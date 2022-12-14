package com.inear.inear.service;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.inear.inear.config.Secret;
import com.inear.inear.model.Alarm;
import com.inear.inear.model.Users;
import com.inear.inear.repository.AlarmRepository;
import com.inear.inear.repository.UserRepository;
import com.inear.model.PatchAlarmReq;
import com.inear.model.PostAlarmReq;
import com.inear.model.PostAlarmRes;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlarmService {

    public static final String bucketName = Secret.ORACLE_BUCKET_NAME;
    public static final String namespaceName = Secret.ORACLE_BUCKET_NAMESPACE;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    @Transactional
    public PostAlarmRes createAlarm(final PostAlarmReq postAlarmReq,final UploadManager uploadManager, final Long userId) throws Exception {
        Users users = userRepository.findById(userId).get();
        Alarm postAlarm = alarmRepository.save(new Alarm(postAlarmReq,users));
//        makeAlarmMp3File(Alarm.convertArrayMsgToStringMsg(postAlarmReq.getMessage()),postAlarm.getAlarmId());
        makeAlarmMp3FileTmp(postAlarm,postAlarm.getAlarmId());
        uploadAlarmMp3File(postAlarm,uploadManager);
        return new PostAlarmRes(postAlarm.getAlarmId());
    }

    public void makeAlarmMp3File(String alarmMessage, Long alarmIdx) throws Exception {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(alarmMessage).build();

            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("ko-KR")
                            .setSsmlGender(SsmlVoiceGender.MALE)
                            .setName("ko-KR-Standard-C")
                            .build();

            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();

            try (OutputStream out = new FileOutputStream("./"+alarmIdx.toString()+".mp3")) {
                out.write(audioContents.toByteArray());
            }
        }
    }

    public void makeAlarmMp3FileTmp(final Alarm postAlarmReq,final Long alarmIdx) throws Exception {
        String alarmMessage = postAlarmReq.getMessage();
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(alarmMessage).build();
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("ko-KR")
                            .setSsmlGender(convertStringToSsmlVoiceGender(postAlarmReq.getVoiceType().getGender()))
                            .setName(postAlarmReq.getVoiceType().getVoiceName())
                            .build();

            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();

            try (OutputStream out = new FileOutputStream("./"+alarmIdx.toString()+".mp3")) {
                out.write(audioContents.toByteArray());
            }
        }
    }

    private SsmlVoiceGender convertStringToSsmlVoiceGender(String voiceGender) {
        return SsmlVoiceGender.valueOf(voiceGender);
    }

    private void uploadAlarmMp3File(Alarm alarm,UploadManager uploadManager) {
        String objectName = alarm.getAlarmId().toString()+".mp3";
        File file = new File("./"+alarm.getAlarmId()+".mp3");
        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName(bucketName)
                        .namespaceName(namespaceName)
                        .objectName(objectName)
                        .putObjectBody(generateStreamFromString("ExampleStreamValue"))
                        .build();

        UploadManager.UploadRequest uploadDetails =
                UploadManager.UploadRequest.builder(file).allowOverwrite(true).build(request);
        uploadManager.upload(uploadDetails);
        deleteFile(file);
    }

    private void deleteFile(File file){
        file.delete();
    }

    private static InputStream generateStreamFromString(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }

    public Alarm findByAlarm(final Long alarmId) {
        return alarmRepository.findById(alarmId).orElse(null);
    }

    @Transactional
    public void modifyAlarm(final Long alarmId, final PatchAlarmReq patchAlarmReq,final UploadManager uploadManager) throws Exception {
        Alarm alarm = findByAlarm(alarmId);
        if(!Alarm.isNullArray(patchAlarmReq.getMessage())) {
            modifyAlarmFile(alarmId,patchAlarmReq,uploadManager);
        }
        new Alarm(alarm,patchAlarmReq);
    }

    @Transactional
    public void modifyAlarmFile(final Long alarmId,final PatchAlarmReq patchAlarmReq,final UploadManager uploadManager) throws Exception {
        Alarm alarm = findByAlarm(alarmId);
        makeAlarmMp3File(Alarm.convertArrayMsgToStringMsg(patchAlarmReq.getMessage()),alarmId);
        uploadAlarmMp3File(alarm,uploadManager);
    }

    @Transactional
    public void deleteAlarm(Long id) {
        Alarm alarm = findByAlarm(id);
        alarmRepository.deleteById(alarm.getAlarmId());
    }

    public List<Alarm> findByAlarms(final Long userId) {
        return alarmRepository.findAlarmsBy(userId);
    }

    @Transactional
    public Alarm toggleAlarmActive(Long id) {
        Alarm alarm = alarmRepository.findById(id).get();
        if(alarm.getActive().equals("Y")){
            new Alarm(alarm, "N");
            return alarm;
        }
        new Alarm(alarm,"Y");
        return alarm;
    }
}
