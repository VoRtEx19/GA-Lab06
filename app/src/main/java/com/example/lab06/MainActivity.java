package com.example.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lab06.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Random random = new Random();
    private Button resetButton;

    private static final Animation[] anims90 = new Animation[4], anims180 = new Animation[4],
            startAnims = new Animation[4];
    private final RotatingEmoji[] emojis = new RotatingEmoji[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.lab06.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resetButton = binding.buttonAnim;

        emojis[0] = new RotatingEmoji(binding.iv1);
        emojis[1] = new RotatingEmoji(binding.iv2);
        emojis[2] = new RotatingEmoji(binding.iv3);
        emojis[3] = new RotatingEmoji(binding.iv4);
        emojis[4] = new RotatingEmoji(binding.iv5);

        loadAnimations();

        for (RotatingEmoji emoji : emojis) {
            emoji.image.setOnClickListener(this);
        }
        resetButton.setOnClickListener(this);

        prepareRotations();
    }

    private void loadAnimations() {
        anims90[0] = AnimationUtils.loadAnimation(this, R.anim.rotate_0to90);
        anims90[1] = AnimationUtils.loadAnimation(this, R.anim.rotate_90to180);
        anims90[2] = AnimationUtils.loadAnimation(this, R.anim.rotate_180to270);
        anims90[3] = AnimationUtils.loadAnimation(this, R.anim.rotate_270to360);

        anims180[0] = AnimationUtils.loadAnimation(this, R.anim.rotate_0to180);
        anims180[1] = AnimationUtils.loadAnimation(this, R.anim.rotate_90to270);
        anims180[2] = AnimationUtils.loadAnimation(this, R.anim.rotate_180to360);
        anims180[3] = AnimationUtils.loadAnimation(this, R.anim.rotate_270to450);

        startAnims[0] = AnimationUtils.loadAnimation(this, R.anim.rotate_0to0);
        startAnims[1] = AnimationUtils.loadAnimation(this, R.anim.rotate_0to90);
        startAnims[2] = AnimationUtils.loadAnimation(this, R.anim.rotate_0to180);
        startAnims[3] = AnimationUtils.loadAnimation(this, R.anim.rotate_0to270);

        for (Animation anim: anims90)
            anim.setFillAfter(true);
        for (Animation anim: anims180)
            anim.setFillAfter(true);
        for (Animation anim: startAnims)
            anim.setFillAfter(true);
    }

    private boolean allLinedUp() {
        if (emojis[0].angle != emojis[1].angle)
            return false;
        if (emojis[1].angle != emojis[2].angle)
            return false;
        if (emojis[2].angle != emojis[3].angle)
            return false;
        return emojis[3].angle == emojis[4].angle;
    }

    private void prepareRotations() {
        resetButton.setClickable(false);
        resetButton.setVisibility(View.INVISIBLE);

        for (RotatingEmoji emoji : emojis) {
            emoji.image.setClickable(true);

            int i = random.nextInt(4);
            emoji.image.startAnimation(startAnims[i]);
            emoji.angle = 90 * i;
        }
        Log.d("Rotate", "Angles " + emojis[0].angle + " " + emojis[1].angle + " " +
                emojis[2].angle + " " + emojis[3].angle + " " + emojis[4].angle);
    }

    private void finishRotations() {
        resetButton.setClickable(true);
        resetButton.setVisibility(View.VISIBLE);

        for (RotatingEmoji emoji : emojis) {
            emoji.image.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != resetButton) {
            int selectedId;
            switch (v.getContentDescription().toString()) {
                case "Emoji 1":
                    selectedId = 0;
                    break;
                case "Emoji 2":
                    selectedId = 1;
                    break;
                case "Emoji 3":
                    selectedId = 2;
                    break;
                case "Emoji 4":
                    selectedId = 3;
                    break;
                case "Emoji 5":
                    selectedId = 4;
                    break;
                default:
                    selectedId = -1;
                    break;
            }
            Log.d("Selected", selectedId + "");
            if (selectedId != -1) {
                for (int i = 0; i < 5; ++i)
                    emojis[i].rotate(i == selectedId);
                if (allLinedUp()) {
                    Toast.makeText(this, "Congrats!", Toast.LENGTH_LONG).show();
                    finishRotations();
                }
                Log.d("Rotate", "Angles " + emojis[0].angle + " " + emojis[1].angle + " " +
                        emojis[2].angle + " " + emojis[3].angle + " " + emojis[4].angle);
            }
        } else {
            prepareRotations();
        }
    }

    private static class RotatingEmoji {
        private final ImageView image;
        private int angle;

        private RotatingEmoji(View v) {
            image = (ImageView) v;
            angle = 0;
        }

        private void rotate(boolean selected) {
            if (selected) {
                image.startAnimation(anims180[angle / 90]);
                angle = (angle + 180) % 360;
            } else {
                image.startAnimation(anims90[angle / 90]);
                angle = (angle + 90) % 360;
            }
        }
    }
}