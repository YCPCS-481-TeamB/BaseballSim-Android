package kylemeyers22.heroku.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import kylemeyers22.heroku.R;
import kylemeyers22.heroku.apiObjects.Game;

public class GameListItemAdapter extends ArrayAdapter<Game> {
    private ArrayList<Game> gameList;

    public GameListItemAdapter(Context context, int textViewResourceID, ArrayList<Game> gameItems) {
        super(context, textViewResourceID, gameItems);
        this.gameList = gameItems;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View convert = convertView;

        if (convert == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convert = inflater.inflate(R.layout.gamerow, null);
        }

        final Game current = gameList.get(position);

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
                // Debug output
                // TODO: Replace with something effective when selecting a game
                System.out.println("Pressed row " + position + " which contains Game " + current.getGameId());
            }
        });

        return convert;
    }
}
