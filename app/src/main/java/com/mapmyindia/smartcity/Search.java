package com.mapmyindia.smartcity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

public class Search extends AppCompatActivity {

    AutoCompleteTextView autoComplete;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.this.finish();
                Intent intent = new Intent(Search.this, GridHome.class);
                startActivity(intent);
            }
        });

        /*AutoSuggestManager autoSuggestManager =new AutoSuggestManager();
        autoSuggestManager.getSuggestions(String.valueOf(autoComplete.getText()), new AutoSuggestListener() {
            @Override
            public void onResult(int code, ArrayList places) {
                //code:0 success, 1 exception, 2 no result
                // response in array of AutoSuggest class
                if(code == 0)   {
                    Log.d("AutoSuggest", "Success");
                    for(int i = 0; i < places.size(); i++)
                        Log.d("AutoSuggest", String.valueOf(places.get(i)));
                }   else if(code == 1)  Log.e("AutoSuggest", "Exception");
                else if(code == 2)  Log.d("AutoSuggest", "No Result");
            }
        },true);
*/
    }
}
