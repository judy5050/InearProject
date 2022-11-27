package com.inear.inear.controller;// Imports the Google Cloud client library

import com.inear.inear.config.Secret;
import com.inear.inear.exception.AlarmException;
import com.inear.inear.model.Alarm;
import com.inear.inear.service.AlarmService;
import com.inear.inear.service.jwt.JwtService;
import com.inear.inear.utils.Message;
import com.inear.inear.utils.Status;
import com.inear.model.GetAlarmRes;
import com.inear.model.PatchAlarmReq;
import com.inear.model.PostAlarmReq;
import com.inear.model.PostAlarmRes;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.PreauthenticatedRequest;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.requests.GetPreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.responses.CreatePreauthenticatedRequestResponse;
import com.oracle.bmc.objectstorage.responses.GetPreauthenticatedRequestResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Google Cloud TextToSpeech API sample application. Example usage: mvn package exec:java
 * -Dexec.mainClass='com.example.texttospeech.QuickstartSample'
 */

@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmService alarmService;
    private final JwtService jwtService;

    private static final String ALARM_CREATE_SUCCESS_MESSAGE = "알람생성완료";
    private static final String ALARM_ESSENTIAL_FIELD_FAIL_MESSAGE = "알람 정보의 필수값을 입력해주세요";
    private static final String ALARM_PATCH_SUCCESS_MESSAGE = "알람수정성공";
    private static final String ALARM_DELETE_SUCCESS_MESSAGE = "알람제거성공";
    private static final String GET_ALARM_LIST_SUCCESS_MESSAGE = "알림리스트 조회";
    @PostMapping("/alarm")
    public ResponseEntity<Message> createAlarm(@RequestBody PostAlarmReq postAlarmReq) throws Exception {
        essentialValueCheck(postAlarmReq);
        Long userId = jwtService.getUserId();

        // 파일 업로드를 위한 초기화 및 세팅
        ConfigFileReader.ConfigFile config = loadObjectStorageConfig();
        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);
        ObjectStorage client = objectStorageInit(provider);
        UploadConfiguration uploadConfiguration = uploadSetting();
        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);
        PostAlarmRes alarm = alarmService.createAlarm(postAlarmReq,uploadManager,userId);
        Message buildMessage = Message.builder().message(ALARM_CREATE_SUCCESS_MESSAGE).data(new PostAlarmRes(alarm.getAlarmId())).status(Status.OK.getStatusCode()).build();
        return new ResponseEntity<>(buildMessage,HttpStatus.OK);
    }

    private UploadConfiguration uploadSetting() {
        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .build();
        return uploadConfiguration;
    }

    private ObjectStorage objectStorageInit(AuthenticationDetailsProvider provider) {
        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.AP_SEOUL_1);
        return client;
    }

    private ConfigFileReader.ConfigFile loadObjectStorageConfig() throws IOException {
        ConfigFileReader.ConfigFile config = ConfigFileReader.parse(Secret.ORACLE_CONFIG_FILE_LOCATION, Secret.ORACLE_CONFIG_FILE_PROFILE);
        return config;
    }

    private void essentialValueCheck(PostAlarmReq postAlarmReq) {
        if(postAlarmReq.getMessage().size() == 0 || postAlarmReq.getAlarmDate().size() == 0 || postAlarmReq.getAlarmTime() == null) {
            throw new AlarmException(ALARM_ESSENTIAL_FIELD_FAIL_MESSAGE);
        }
    }

    @PatchMapping("/alarms/{id}")
    public ResponseEntity<Message> patchAlarm(@PathVariable Long id, @RequestBody PatchAlarmReq patchAlarmReq) throws Exception {
        jwtService.getUserId();
        // 파일 업로드를 위한 초기화 및 세팅
        ConfigFileReader.ConfigFile config = loadObjectStorageConfig();
        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);
        ObjectStorage client = objectStorageInit(provider);
        UploadConfiguration uploadConfiguration = uploadSetting();
        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

        alarmService.modifyAlarm(id,patchAlarmReq,uploadManager);
        Message message = Message.builder().message(ALARM_PATCH_SUCCESS_MESSAGE).status(Status.OK.getStatusCode()).build();
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    @DeleteMapping("/alarms/{id}")
    public ResponseEntity<Message> deleteAlarm(@PathVariable Long id){
        jwtService.getUserId();
        alarmService.deleteAlarm(id);
        Message message = Message.builder().message(ALARM_DELETE_SUCCESS_MESSAGE).status(Status.OK.getStatusCode()).build();
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    @GetMapping("/alarms")
    public ResponseEntity<Message> getAlarms(){
        Long userId = jwtService.getUserId();
        List<Alarm> alarms = alarmService.findByAlarms(userId);
        List<GetAlarmRes> getAlarmsRes = alarms.stream().map(GetAlarmRes::new).collect(Collectors.toList());
        Message message = Message.builder().message(GET_ALARM_LIST_SUCCESS_MESSAGE).status(Status.OK.getStatusCode()).data(getAlarmsRes).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/alarms/{id}")
    public ResponseEntity<Message> getAlarms(@PathVariable Long id){
        jwtService.getUserId();
        Alarm alarm = alarmService.findByAlarm(id);
        Message message = Message.builder().message(GET_ALARM_LIST_SUCCESS_MESSAGE).status(Status.OK.getStatusCode()).data(new GetAlarmRes(alarm)).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

}