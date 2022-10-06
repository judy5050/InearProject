package com.inear.inear.controller;// Imports the Google Cloud client library

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.inear.inear.service.AlarmService;
import com.inear.model.PostAlarmReq;
import com.inear.model.PostAlarmRes;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Google Cloud TextToSpeech API sample application. Example usage: mvn package exec:java
 * -Dexec.mainClass='com.example.texttospeech.QuickstartSample'
 */

@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    /** Demonstrates using the Text-to-Speech API. */
//    @GetMapping("/alarm")
//    public void callAlarm() throws Exception {
//        // Instantiates a client
//        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
//            // Set the text input to be synthesized
//            SynthesisInput input = SynthesisInput.newBuilder().setText("Hello, World!").build();
//
//            // Build the voice request, select the language code ("en-US") and the ssml voice gender
//            // ("neutral")
//            VoiceSelectionParams voice =
//                    VoiceSelectionParams.newBuilder()
//                            .setLanguageCode("en-US")
//                            .setSsmlGender(SsmlVoiceGender.NEUTRAL)
//                            .build();
//
//
//            // Select the type of audio file you want returned
//            AudioConfig audioConfig =
//                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
//
//            // Perform the text-to-speech request on the text input with the selected voice parameters and
//            // audio file type
//            SynthesizeSpeechResponse response =
//                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
//
//            // Get the audio contents from the response
//            ByteString audioContents = response.getAudioContent();
//
//            // Write the response to the output file.
//            try (OutputStream out = new FileOutputStream("output.mp3")) {
//                out.write(audioContents.toByteArray());
//                System.out.println("Audio content written to file \"output.mp3\"");
//            }
//        }
//    }

//    @GetMapping("/alarm")
//    public void getAlarm() throws IOException {
//        /**
//         * Create a default authentication provider that uses the DEFAULT
//         * profile in the configuration file.
//         * Refer to <see href="https://docs.cloud.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm#SDK_and_CLI_Configuration_File>the public documentation</see> on how to prepare a configuration file.
//         */
//        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
//        final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
//
//        /* Create a service client */
//        ObjectStorageClient client = new ObjectStorageClient(provider);
//
//        /* Create a request and dependent object(s). */
//
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .namespaceName("EXAMPLE-namespaceName-Value")
//                .bucketName("EXAMPLE-bucketName-Value")
//                .objectName("EXAMPLE-objectName-Value")
//                .versionId("ocid1.test.oc1..<unique_ID>EXAMPLE-versionId-Value")
//                .ifMatch("EXAMPLE-ifMatch-Value")
//                .ifNoneMatch("EXAMPLE-ifNoneMatch-Value")
//                .opcClientRequestId("ocid1.test.oc1..<unique_ID>EXAMPLE-opcClientRequestId-Value")
//                .opcSseCustomerAlgorithm("EXAMPLE-opcSseCustomerAlgorithm-Value")
//                .opcSseCustomerKey("EXAMPLE-opcSseCustomerKey-Value")
//                .opcSseCustomerKeySha256("EXAMPLE-opcSseCustomerKeySha256-Value")
//                .httpResponseContentDisposition("EXAMPLE-httpResponseContentDisposition-Value")
//                .httpResponseCacheControl("EXAMPLE-httpResponseCacheControl-Value")
//                .httpResponseContentType("EXAMPLE-httpResponseContentType-Value")
//                .httpResponseContentLanguage("EXAMPLE-httpResponseContentLanguage-Value")
//                .httpResponseContentEncoding("EXAMPLE-httpResponseContentEncoding-Value")
//                .httpResponseExpires("EXAMPLE-httpResponseExpires-Value").build();
//
//        /* Send request to the Client */
//        GetObjectResponse response = client.getObject(getObjectRequest);
//    }

    @PutMapping("/alarm")
    public void alarmTest() throws Exception {

        ConfigFileReader.ConfigFile config = ConfigFileReader.parse("~/.oci/config", "DEFAULT");

        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);

        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.AP_SEOUL_1);

        String namespaceName = "cnddmn2sn79l";
        String bucketName =  "inear-bucket";
        String objectName = "test_file.mp3";
        Map<String, String> metadata = null;
//        String contentType = "image/jpg";

        String contentEncoding = null;
        String contentLanguage = null;

        File body = new File("/output.mp3");

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName(bucketName)
                        .namespaceName(namespaceName)
                        .objectName(objectName)
//                        .contentType(contentType)
                        .contentLanguage(contentLanguage)
                        .putObjectBody(generateStreamFromString("ExampleStreamValue"))
                        .contentEncoding(contentEncoding)
                        .opcMeta(metadata)
                        .build();

        PutObjectResponse response = client.putObject(request);

    }


    private static InputStream generateStreamFromString(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

    }
//
//    @PostMapping("/alarm")
//    public ResponseEntity<PostAlarmRes> createAlarm(@RequestBody PostAlarmReq postAlarmReq) {
//        if(notNullFieldValidate(postAlarmReq)) {
//            throw new IllegalArgumentException("message, alarmTime. alarmDate 는 필수값입니다.");
//        }
//        return new ResponseEntity<PostAlarmRes>(alarmService.createAlarm(postAlarmReq), HttpStatus.OK);
//
//    }
//
//    private boolean notNullFieldValidate(PostAlarmReq postAlarmReq) {
//        return postAlarmReq.getAlarmDate() == null || postAlarmReq.getAlarmTime() == null || postAlarmReq.getMessage() == null;
//    }
}