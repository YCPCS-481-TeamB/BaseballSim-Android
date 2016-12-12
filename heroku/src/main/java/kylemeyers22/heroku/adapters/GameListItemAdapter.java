package kylemeyers22.heroku.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import kylemeyers22.heroku.R;
import kylemeyers22.heroku.apiControllers.GameController;
import kylemeyers22.heroku.apiObjects.Game;

public class GameListItemAdapter extends ArrayAdapter<Game> {
    private ArrayList<Game> gameList;
    private SharedPreferences preferences = getContext().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
    private String authToken = preferences.getString("apiToken", null);

    public GameListItemAdapter(Context context, int textViewResourceID, ArrayList<Game> gameItems) {
        super(context, textViewResourceID, gameItems);
        this.gameList = gameItems;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View convert = convertView;

        if (convert == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convert = inflater.inflate(R.layout.gamerow, parent, false);
        }

        final Game current = gameList.get(position);
        final GameController gameCont = new GameController(authToken);

        if (current != null) {
            TextView gameDisplay = (TextView) convert.findViewById(R.id.gameDisplay);
            TextView teamOneDisplay = (TextView) convert.findViewById(R.id.teamOneDisplay);
            TextView teamTwoDisplay = (TextView) convert.findViewById(R.id.teamTwoDisplay);

            gameDisplay.setText(getContext().getString(R.string.game_header, current.getGameId()));
            System.out.println("Team one from current game: " + current.getTeamOne());
            teamOneDisplay.setText(String.format(Locale.getDefault(), "Team %d", current.getTeamOne()));
            teamTwoDisplay.setText(String.format(Locale.getDefault(), "Team %d", current.getTeamTwo()));
        }

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder startPrompt = new AlertDialog.Builder(v.getContext());
                    if (gameCont.isGameApproved(current)) {
                        if (!gameCont.isGameStarted(current)) {
                            // Prompt to start the selected game
                            startPrompt.setTitle("Start Game");
                            startPrompt.setMessage("Would you like to start this game?");
                            startPrompt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        gameCont.startGame(current);
                                    } catch (IOException iexc) {
                                        System.out.println("Could not start game!");
                                        iexc.printStackTrace();
                                    }
                                }
                            });
                            startPrompt.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            startPrompt.show();
                        } else {
                            System.out.println("Game already started");
                        }
                    } else {
                        // Game not yet approved
                        startPrompt.setTitle("Not Approved");
                        startPrompt.setMessage("This game hasn't been approved by all participants yet");
                        startPrompt.show();
                        startPrompt.setCancelable(true);
                    }
                } catch (IOException | JSONException exc) {
                    exc.printStackTrace();
                }
            }
        });

        return convert;
    }
}
