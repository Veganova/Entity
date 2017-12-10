package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.R;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Set of classes that make up the menu that the player can open and close and select items to build
 */
public class Menu extends RelativeLayout {

    public Menu(Context context, int parentHeight, Player player, int gravity) {
        super(context);

        this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        this.setGravity(gravity);

        MenuList listing = new MenuList(context, parentHeight, player);
        this.addView(listing);
    }
}

class MenuList extends PopListing {

    private final Player player;

    public MenuList(Context context, int parentHeight, Player player) {
        super(context, parentHeight);

        this.player = player;

        super.create(context);
    }

    @Override
    protected Poppist createListing(Context context) {
        return new MenuList.MenuItems(context, Arrays.asList(Entities.values()), player);
    }

    @Override
    protected Popper createPopper(Context context, Poppist listing) {
        return new MenuList.MenuPop(context, listing);
    }


    class MenuPop extends Popper {

        MenuPop(Context context, final Poppist toPop) {
            super(context, toPop, R.drawable.ic_down_arrow, 1);
        }
    }

    class MenuItems extends Poppist {


        MenuItems(Context context, List<Entities> toDisplay, Player player) {
            super(context, 0, 0.75, 600);

            for (Entities entityType : toDisplay) {
                EntScroll entScroll = new EntScroll(context, entityType, player);
                container.addView(entScroll);
            }
        }

        private class EntScroll extends ScrollView {

            public EntScroll(Context context, final Entities entType, Player player) {
                super(context);

                this.setVerticalScrollBarEnabled(false);

                LinearLayout container = new LinearLayout(context);
                container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                container.setOrientation(LinearLayout.VERTICAL);

                for (EntityLeaf producer : entType.produceables) {
                    container.addView(new EntButton(context, producer, player));
                }


                this.addView(container);
            }
        }

        private class EntButton extends TextView {

            public EntButton(Context context, final EntityLeaf toProduce, final Player owner) {
                super(context);

                this.setGravity(Gravity.CENTER);
                this.setPadding(50, 0, 50, 0);

                this.setTextColor(GamePanel.background_dark);
                this.setText(toProduce.name);

                // TODO: 8/25/2017 for now manually setting the height.. might be kinda bad idk.
                this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        MenuList.this.HEIGHT));

                this.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        owner.setGhost(toProduce);
                    }
                });
            }
        }

    }
}