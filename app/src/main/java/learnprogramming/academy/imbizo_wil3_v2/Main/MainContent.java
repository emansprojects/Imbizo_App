package learnprogramming.academy.imbizo_wil3_v2.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import learnprogramming.academy.imbizo_wil3_v2.Fragments.HomeFragment;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;


public class MainContent extends YouTubeBaseActivity {

    FirebaseDatabase databaseCategories;
    FirebaseStorage storage;
    DatabaseReference refCategories, refSavedLectures, newPost;
    StorageReference storageRef;
    public static String checkBM1;
    String videoLink;
    YouTubePlayerView  ytPlayer;


    @Override
    public void onStart() {
        super.onStart();

        MainActivity.checkView = 2;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainContent.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_content);
        ytPlayer = (YouTubePlayerView)findViewById(R.id.ytPlayerFragment);

        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.bookmark);
        if(checkBM1.equals("show"))
        {
            b.show();
        }
        if(checkBM1.equals("hide"))
        {
            b.hide();
        }

        loadData();

        refSavedLectures = databaseCategories.getReference("SavedLectures");
        if(b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String[] tokens = HomeFragment.usernameEmail.split("@");
                    newPost = refSavedLectures.child(tokens[0] + MainLectures.l.getLectureName());




                    //Code Attributtion
                    //Author: GeeksforGeeks
                    //Website:GeeksforGeeks
                    //Topic:How to Save Data to the Firebase Realtime Database in Android?
                    //Link:https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/#:~:text=Run%20the%20app%20and%20make,added%20to%20our%20Firebase%20Database.

                    newPost.child("lectureName").setValue(MainLectures.l.getLectureName());
                    newPost.child("lectureImage").setValue(MainLectures.l.getLectureImage());
                    newPost.child("lecturePDF").setValue(videoLink);
                    newPost.child("typeOfContent").setValue(MainLectures.l.getTypeOfContent());
                    newPost.child("userName").setValue(HomeFragment.usernameEmail);
                    newPost.child("categoryType").setValue(MainLectures.l.getCategoryType());



                    Toast.makeText(MainContent.this, "Lecture Bookmarked!", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }





    public void loadData() {

        databaseCategories = FirebaseDatabase.getInstance();
        refCategories = databaseCategories.getReference("Lectures");
        storage = FirebaseStorage.getInstance();

        refCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot s : snapshot.getChildren()){
                    if(s.child("lectureName").getValue().toString().equals(MainLectures.lectureName) && s.child("categoryType").getValue().toString().equals(HomeFragment.categoryName) ) {

                        videoLink = s.child("link").getValue().toString();

                        String[] tokens = videoLink.split("/");

                        ytPlayer.initialize(
                                "AIzaSyBVZQ-jkkKwGwzDlgAQgYs8f88mn_ObDV4",
                                new YouTubePlayer.OnInitializedListener() {
                                    // Implement two methods by clicking on red
                                    // error bulb inside onInitializationSuccess
                                    // method add the video link or the playlist
                                    // link that you want to play In here we
                                    // also handle the play and pause
                                    // functionality
                                    @Override
                                    public void onInitializationSuccess(
                                            YouTubePlayer.Provider provider,
                                            YouTubePlayer youTubePlayer, boolean b)
                                    {
                                        if(tokens.length <= 1)
                                        {
                                            youTubePlayer.loadVideo(s.child("link").getValue().toString());
                                            youTubePlayer.play();
                                        }
                                        else {
                                            youTubePlayer.loadVideo(tokens[3]);
                                            youTubePlayer.play();
                                        }
                                    }

                                    // Inside onInitializationFailure
                                    // implement the failure functionality
                                    // Here we will show toast
                                    @Override
                                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                        YouTubeInitializationResult
                                                                                youTubeInitializationResult)
                                    {
                                        Toast.makeText(MainContent.this, "Video player Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}