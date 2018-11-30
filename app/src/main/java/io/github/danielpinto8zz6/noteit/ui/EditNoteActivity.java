package io.github.danielpinto8zz6.noteit.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.kizitonwose.colorpreference.ColorDialog;
import com.kizitonwose.colorpreference.ColorShape;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.Utils;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;
import io.github.danielpinto8zz6.noteit.notification.NotificationHelper;

import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ARCHIVED;

public class EditNoteActivity extends AppCompatActivity implements ColorDialog.OnColorSelectedListener {
    private static final String TAG = "NoteIt";
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private Note note;
    private boolean editing = false;
    private boolean force = false;
    private TextView titleTv;
    private TextView contentTv;
    private boolean notify = false;

    private SwitchDateTimeDialogFragment dateTimeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleTv = findViewById(R.id.note_title_input);
        contentTv = findViewById(R.id.note_content_input);
        TextView dateTv = findViewById(R.id.note_date);

        dateTv.setText(Utils.getCurrentDateTime());

        Intent intent = getIntent(); // gets the previously created intent
        note = intent.getParcelableExtra("note");

        if (note != null) {
            editing = true;

            titleTv.setText(note.getTitle());
            contentTv.setText(note.getContent());
            String editedDate = note.getEdited_date();
            if (editedDate != null)
                dateTv.setText(editedDate);
            else
                dateTv.setText(note.getCreate_date());

            String color = note.getColor();
            if (color != null) {
                setToolbarColor(Color.parseColor(color));
            }
        } else {
            note = new Note();
        }

        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel)
            );
        }

        dateTimeFragment.setTimeZone(TimeZone.getDefault());
        dateTimeFragment.set24HoursMode(true);
        dateTimeFragment.setMinimumDateTime(Calendar.getInstance().getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                Log.d(TAG, Utils.getDateTime(date));
                note.setNotify_date(Utils.getDateTime(date));
                notify = true;
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.icons_edit_note, menu);

        if (note != null && note.getStatus() != STATUS_ACTIVE) {
            menu.findItem(R.id.action_archive).setVisible(false);
        } else {
            menu.findItem(R.id.action_unarchive).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_palette) {
            new ColorDialog.Builder(this)
                    .setColorShape(ColorShape.CIRCLE) //CIRCLE or SQUARE
                    .setColorChoices(R.array.color_choices) //an array of colors
                    .setSelectedColor(Color.GREEN) //the checked color
                    .setTag("TAG") // tags can be useful when multiple components use the picker within an activity
                    .show();
        } else if (id == R.id.action_archive) {
            note.setStatus(STATUS_ARCHIVED);
            force = true;
            NoteDao.updateRecord(note);
            finish();
        } else if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.delete_confirmation))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            NoteDao.deleteRecord(note);
                            force = true;
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        } else if (id == R.id.action_add_alert) {
            dateTimeFragment.startAtCalendarView();
            dateTimeFragment.setDefaultDateTime(Calendar.getInstance().getTime());
            dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
        } else if (id == R.id.action_unarchive) {
            note.setStatus(STATUS_ACTIVE);
            force = true;
            NoteDao.updateRecord(note);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (!force) {
            if (editing) {
                updateNote();
            } else {
                addNote();
                editing = true;
            }
            if (notify && note.getId() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(Utils.getDateFromString(note.getNotify_date()));
                NotificationHelper.setNotification(getApplicationContext(), cal.getTimeInMillis(), note.getId());
                notify = false;
            }
        }

        super.onPause();
    }

    private void addNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        if (title.isEmpty() && content.isEmpty())
            return;

        note.setTitle(title);
        note.setContent(content);
        note.setId((int) NoteDao.insertRecord(note));
    }

    private void updateNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        String orig = note.toString();
        note.setTitle(title);
        note.setContent(content);

        if (!orig.equals(note.toString())) {
            note.setEdited_date(Utils.getCurrentDateTime());
        }

        NoteDao.updateRecord(note);
    }

    @Override
    public void onColorSelected(int i, String s) {
        String color = Utils.getColorHex(i);
        note.setColor(color);

        setToolbarColor(i);
    }

    private void setToolbarColor(int c) {
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(c));

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(c);

    }
}
