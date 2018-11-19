package com.saifi369.olschoolnotes.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.saifi369.olschoolnotes.utils.SampleDataProvider;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {


    public static final String TAG = "MyTag";
    private static AppRepository ourInstance;
    private AppDatabase mDatabase;

    public LiveData<List<NoteEntity>> mNotesList;
    private Executor mExecutor = Executors.newSingleThreadExecutor();


    public static AppRepository getInstance(Context context) {
        return ourInstance = new AppRepository(context);
    }

    private AppRepository(Context context) {
        mDatabase=AppDatabase.getInstance(context);
        mNotesList=getAllNotes();

    }

    public void addSampleData() {

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.notesDao().insertAll(SampleDataProvider.getSampleData());
            }
        });
    }
    private LiveData<List<NoteEntity>> getAllNotes(){
       return mDatabase.notesDao().getAllNotes();
    }

    public void deleteAllData() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int notes = mDatabase.notesDao().deleteAllNotes();
                Log.d(TAG, "run: Noted Deleted: "+notes);
            }
        });
    }

    public NoteEntity loadNote(int noteId) {
        return mDatabase.notesDao().getNoteById(noteId);
    }

    public void insertNote(final NoteEntity noteEntity) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.notesDao().insertNote(noteEntity);
            }
        });
    }
}
