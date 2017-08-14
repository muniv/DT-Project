package com.muni.examples.aibrilcall;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javaFlacEncoder.FLAC_FileEncoder;

public class CallRecoderService extends Service{
    private static final int RECORDER_BPP = 16;

    private static final String AUDIO_RECODER_FILE_EXT_WAV = "record_temp.wav";
    private static final String AUDIO_RECODER_FILE_EXT_FLAC = ".flac";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";


    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;  //STEREO는 VOICE_UPLINK 나 VOICE_DOWNLINK만 가능 , 채널 2
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static String TAG = "Logcat";
    private AudioRecord recorder = null;
    private int bufSize = 0;
    private int currentFormat = 0;
    private int recorderState = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;


    public IBinder onBind(Intent intent){
        return null;
    }

    public void onCreate(){
        Log.d(TAG, "ServiceCreate");
        super.onCreate();
        bufSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
    }


    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "ServiceStart");
        super.onStartCommand(intent, flags, startId);
        startRecording();
        return  START_STICKY;
    }

    public void onDestroy(){
        Log.d(TAG, "ServiceStop");
        stopRecording();
        super.onDestroy();
    }


    public void startRecording() {

        Log.d(TAG, "startRecording");
        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_CALL, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufSize);
        recorderState = recorder.getState();

        if(recorderState == 1) {

            recorder.startRecording();

            isRecording = true;

            recordingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    writeAudioDataToFile();
                }
            }, "Recorder Thread");

            recordingThread.start();
        }
    }

    private void writeAudioDataToFile(){
        byte data[] = new byte[bufSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try{
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int read = 0;

        if(os != null){
            while (isRecording){
                read = recorder.read(data, 0, bufSize);

                if(AudioRecord.ERROR_INVALID_OPERATION != read){
                    try{
                        os.write(data);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            try{
                os.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void stopRecording() {

        if (recorder != null) {
            recorderState = recorder.getState();


            Log.d(TAG, "stopRecording");
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
            stopSelf();

        }

        copyWaveFile(getTempFilename(), getWavFilename());
        deleteTempFile();
        convertWavToFlac(getWavFilename(), getFlacFilename());
        deleteWavFile();
    }

    public String getWavFilename() {
        Log.d(TAG, "getWAVFilename");
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }


        File tempFile = new File(filepath, AUDIO_RECODER_FILE_EXT_WAV);

        if (tempFile.exists()){
            tempFile.delete();
        }

        return (file.getAbsolutePath() + "/" + AUDIO_RECODER_FILE_EXT_WAV);

    }

    public String getFlacFilename() {
        Log.d(TAG, "getFLACFilename");
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }


        File tempFile = new File(filepath, AUDIO_RECODER_FILE_EXT_FLAC);

        if (tempFile.exists()){
            tempFile.delete();
        }

        //return (file.getAbsolutePath() + "/" + AUDIO_RECODER_FILE_EXT_FLAC);
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECODER_FILE_EXT_FLAC);
    }
    private String getTempFilename() {
        Log.d(TAG, "getTempFilename");
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);

        if (tempFile.exists()){
            tempFile.delete();
        }

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());

        file.delete();
    }


    private void deleteWavFile() {
        File file = new File(getWavFilename());

        file.delete();
    }


    private void copyWaveFile(String inFilename, String outFilename){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

        byte[] data = new byte[bufSize];

        try{
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            Log.d(TAG, "File size :" + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);

            while (in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    private void convertWavToFlac(String inFilename, String outFilename){
        FLAC_FileEncoder flac_encoder = new FLAC_FileEncoder();

        File inputFile = new File(inFilename);
        File outputFile = new File(outFilename);

        flac_encoder.encode(inputFile, outputFile);

        Log.d(TAG, "Encoding wav file to flac file");

    }

}