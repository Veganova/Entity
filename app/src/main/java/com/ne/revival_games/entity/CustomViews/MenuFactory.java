package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.Modes.BaseMode;
import com.ne.revival_games.entity.Modes.GameMode;
import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veganova on 4/22/18.
 */

public class MenuFactory {

    /**
     * value:String     Game mode / indicates what the run. BaseMode object contains the text that will be displayed in the UI.
     */
    private List<BaseMode> rawButtons;
    private Class<? extends BaseMode> listingType;

    /**
     * Provide the type of mode this listing will be presenting.
     */
    public MenuFactory(Class<? extends BaseMode> listingType) {
        this.listingType = listingType;
        rawButtons = new ArrayList<>();
    }

    /**
     * Add on a new pair of text to display and the game mode that it maps to.
     */
    public MenuFactory addButton(BaseMode mode) {
        rawButtons.add(mode);
        return this;
    }

    /**
     * @param caller  The activity that is currently running
     * @param activityToCall The main menu activity class which will be used (ex. MainActivity.class).
     */
    public View build(final Context context, final MainActivity caller, final Class activityToCall) {
        RelativeLayout rl = new RelativeLayout(context);
        rl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rl.setGravity(Gravity.CENTER);


        LinearLayout buttonList = new LinearLayout(context);
        buttonList.setOrientation(LinearLayout.VERTICAL);
        buttonList.setGravity(Gravity.CENTER);
        for (final BaseMode mode: rawButtons) {
            Button button = new Button(context);
            button.setText(mode.toString());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(context, activityToCall);
                    intent.putExtra("enumType", listingType.getSimpleName());
                    intent.putExtra("enumVal", mode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    // wait for the current activity thread to end. (so that it will let go of the canvas lock)
                    caller.myThread.end();
                    if (caller.myThread.hasEnded()) {

                    } else {
                        try {
                            synchronized (caller.myThread) {
                                caller.myThread.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    caller.startActivity(intent);
                }
            });
            this.formatButton(button);
            buttonList.addView(button);
        }
        rl.addView(buttonList);
        return rl;
    }

    private void formatButton(Button button) {
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setTextColor(GamePanel.cream);
        button.setTextSize(18);
    }
}
