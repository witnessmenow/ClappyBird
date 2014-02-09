package com.ladinc.clappybird.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioRecorder;
import com.ladinc.clappybird.core.objects.Bird;
import com.musicg.api.ClapApi;
import com.musicg.wave.WaveHeader;

public class AudioThread implements Runnable{

	//part of musicg jar
    private ClapApi clapApi;
    
    AudioRecorder recorder;
    
    Bird bird;
    
    private static final short[] shortPCM = new short[1024]; // 1024 samples
    private WaveHeader waveHeader;
    
    public AudioThread(Bird bird){
    	///This will create an AudioRecorder with a sampling rate of 22.05khz, in mono mode. 
        //If the recorder couldn't be created, a GdxRuntimeException will be thrown.
        recorder = Gdx.audio.newAudioRecorder(22050, true);
        
        //Set up for clapApi, part of musicg jar, which detetcs claps
        
        //TODO Set this up properly
        waveHeader = new WaveHeader();
        waveHeader.setChannels(1);
        waveHeader.setBitsPerSample(16);
        waveHeader.setSampleRate(22050);
        
        clapApi = new ClapApi(waveHeader);
        
        this.bird = bird;
    }
    
    //This should work out whether there was a clap or not and call jump then on the Bird object
	@Override
	public void run() {
		while(true)
		{	
			//read in audio data to be analysed
			recorder.read(shortPCM, 0, shortPCM.length);
			
			byte[] byteArr = this.convertShortToByteArr(shortPCM);
			
			//need to convert short[] to byte[] for isClap
			if(this.clapApi!=null){
				//TODO: Throwing an exception at the moment. Prob to do with setting up the clapApi properly (wave header)
				//if(this.clapApi.isClap(byteArr))
				//{
				//	bird.jump();
				//}

				if(Gdx.input.justTouched())
				{			
					bird.jump();
				}
			}
		}
	}
	
	private byte[] convertShortToByteArr(short[] shrt_array) {
		ByteBuffer buffer = ByteBuffer.allocate(shrt_array.length * 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.asShortBuffer().put(shrt_array);
		byte[] bytes = buffer.array();
		return bytes;
	}

}
