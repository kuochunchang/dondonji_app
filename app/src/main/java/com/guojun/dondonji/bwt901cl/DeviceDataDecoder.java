package com.example.guojun.dondonji.bwt901cl;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class DeviceDataDecoder {

    private Queue<Byte> queueBuffer = new LinkedList<Byte>();
    private DecodedDataListener decodedDataListener;

    public interface DecodedDataListener {
        void onDataDecoded(com.example.guojun.dondonji.bwt901cl.SensorData data);
    }

    public DeviceDataDecoder(DecodedDataListener listener){
       this.decodedDataListener = listener;
    }



    com.example.guojun.dondonji.bwt901cl.SensorData data =  new com.example.guojun.dondonji.bwt901cl.SensorData();
    public void putRawData(byte[] raw, int length){
        for (int i = 0; i < length; i++) queueBuffer.add(raw[i]);
        while (queueBuffer.size() >= 11) {

            if ((queueBuffer.poll()) != 0x55) continue;

            byte sHead = queueBuffer.poll();
            byte[] packBuffer = new byte[11];

            for (int j = 0; j < 9; j++){
                packBuffer[j] = queueBuffer.poll();
            }

            switch (sHead) {//
                case 0x50:
                    int ms = ((((short) packBuffer[7]) << 8) | ((short) packBuffer[6] & 0xff));
                    data.date = String.format("20%02d-%02d-%02d",packBuffer[0],packBuffer[1],packBuffer[2]);
                    data.time = String.format(" %02d:%02d:%02d.%03d",packBuffer[3],packBuffer[4],packBuffer[5],ms);
                    break;
                case 0x51: //加速度
                    data.accelerationX = ((((short) packBuffer[1]) << 8) | ((short) packBuffer[0] & 0xff)) / 32768.0f * 16;
                    data.accelerationY = ((((short) packBuffer[3]) << 8) | ((short) packBuffer[2] & 0xff)) / 32768.0f * 16;
                    data.accelerationZ = ((((short) packBuffer[5]) << 8) | ((short) packBuffer[4] & 0xff)) / 32768.0f * 16;
                    data.temperature = ((((short) packBuffer[7]) << 8) | ((short) packBuffer[6] & 0xff)) / 100.0f;
                    break;
                case 0x52: // 角速度
                    data.angularVelocityX = ((((short) packBuffer[1]) << 8) | ((short) packBuffer[0] & 0xff)) / 32768.0f * 2000;
                    data.angularVelocityY = ((((short) packBuffer[3]) << 8) | ((short) packBuffer[2] & 0xff)) / 32768.0f * 2000;
                    data.angularVelocityZ = ((((short) packBuffer[5]) << 8) | ((short) packBuffer[4] & 0xff)) / 32768.0f * 2000;
                    data.temperature = ((((short) packBuffer[7]) << 8) | ((short) packBuffer[6] & 0xff)) / 100.0f;
                    break;
                case 0x53: //角度
                    data.angleX = ((((short) packBuffer[1]) << 8) | ((short) packBuffer[0] & 0xff)) / 32768.0f * 180;
                    data.angleY = ((((short) packBuffer[3]) << 8) | ((short) packBuffer[2] & 0xff)) / 32768.0f * 180;
                    data.angleZ = ((((short) packBuffer[5]) << 8) | ((short) packBuffer[4] & 0xff)) / 32768.0f * 180;
                    data.temperature = ((((short) packBuffer[7]) << 8) | ((short) packBuffer[6] & 0xff)) / 100.0f;
                    break;
                case 0x54://磁场
                    data.magneticX = ((((short) packBuffer[1]) << 8) | ((short) packBuffer[0] & 0xff));
                    data.magneticY = ((((short) packBuffer[3]) << 8) | ((short) packBuffer[2] & 0xff));
                    data.magneticZ = ((((short) packBuffer[5]) << 8) | ((short) packBuffer[4] & 0xff));
                    data.temperature = ((((short) packBuffer[7]) << 8) | ((short) packBuffer[6] & 0xff)) / 100.0f;
                    break;
                case 0x59://四元数
                    data.quaternions0 = ((((short) packBuffer[1]) << 8) | ((short) packBuffer[0] & 0xff)) / 32768.0f;
                    data.quaternions1 = ((((short) packBuffer[3]) << 8) | ((short) packBuffer[2] & 0xff))/32768.0f;
                    data.quaternions2 = ((((short) packBuffer[5]) << 8) | ((short) packBuffer[4] & 0xff))/32768.0f;
                    data.quaternions3 = ((((short) packBuffer[7]) << 8) | ((short) packBuffer[6] & 0xff))/32768.0f;
                    break;
            }
        }

        decodedDataListener.onDataDecoded(data);



    }
}
