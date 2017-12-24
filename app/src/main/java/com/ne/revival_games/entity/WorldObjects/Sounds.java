package com.ne.revival_games.entity.WorldObjects;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.ne.revival_games.entity.R;

import java.util.HashMap;

/**
 * Created by veganova on 12/21/17.
 */

public class Sounds {
    private AudioAttributes audioAttributes;

    private static Sounds ourInstance;
    private static HashMap<SOUND_TYPE, Integer> soundMap;
    private static SoundPool soundPool;

    public enum SOUND_TYPE {SHOOT, HIT, WIN, LOSE, MODE}

    /**
     * Pass in null for regular usage. The first usage should pass in a valid context.
     *
     * @param context
     * @return
     */
    public static Sounds getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new Sounds(context);
        }
        return ourInstance;
    }
    
//    // Make sure to set context before caling sounds
//    public void setContext(Context context) {
//        this.context = context;
//    }

    private Sounds(Context context) {
        System.out.println("NEW SOUND_TYPE CREATED!!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_TYPE.values().length)
                    .build();
        } else {
            soundPool = new SoundPool(SOUND_TYPE.values().length, AudioManager.STREAM_MUSIC, 0);
        }


        soundMap = new HashMap<>();
        soundMap.put(SOUND_TYPE.SHOOT, soundPool.load(context, R.raw.shoot, 1));
        soundMap.put(SOUND_TYPE.HIT, soundPool.load(context, R.raw.hit, 1));
        soundMap.put(SOUND_TYPE.WIN, soundPool.load(context, R.raw.round_win, 1));
        soundMap.put(SOUND_TYPE.LOSE, soundPool.load(context, R.raw.round_lose, 1));
        soundMap.put(SOUND_TYPE.MODE, soundPool.load(context, R.raw.gamemode, 1));
    }

    public void playSound(SOUND_TYPE s) {
//        if (this.context == null) {
//            throw new NullPointerException("The context is note set for the Sound class.");
//        }
        int soundId = soundMap.get(s);
        soundPool.play(soundId, 0.2f, 0.5f, 1, 0, 1.0f);
    }
}
