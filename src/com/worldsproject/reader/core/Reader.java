package com.worldsproject.reader.core;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Reader extends Activity implements View.OnClickListener
{
	private ImageButton backward = null;
	private ImageButton play = null;
	private ImageButton forward = null;

	private TextView read = null;

	private WordLoader wl = null;

	private final Handler hand = new Handler();
	private Runnable runner;

	private boolean playing = false;

	private int wpm = 60;

	private final int FORWARD_DIALOG = 0;
	private final int BACKWARD_DIALOG = 1;
	private final int SPEED_DIALOG = 2;
	
	ProgressDialog dialog = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);

		setContentView(R.layout.main);

		backward = (ImageButton)findViewById(R.id.backward);
		play = (ImageButton)findViewById(R.id.play);
		forward = (ImageButton)findViewById(R.id.forward);

		read = (TextView)findViewById(R.id.read);

		backward.setOnClickListener(this);
		play.setOnClickListener(this);
		forward.setOnClickListener(this);

		wl = new WordLoader();

		runner = new Runnable()
		{
			public void run()
			{
				wl.nextWord();
				updateWord();

				hand.postDelayed(this, 60000/wpm);
			}
		};
	}

	public void onClick(View view) 
	{
		if(view == backward)
		{
			wl.moveBackward();
		}
		else if(view == forward)
		{
			wl.moveForward();
		}
		else if(view == play)
		{
			if(playing)
			{
				pause();
			}
			else
			{
				play();
			}

			playing = !playing;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) 
	{
		this.pause();
		new MenuInflater(getApplication()).inflate(R.menu.main, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.backwardAmount:
			this.onCreateDialog(this.BACKWARD_DIALOG);
			return true;
		case R.id.forwardAmount:
			this.onCreateDialog(this.FORWARD_DIALOG);
			return true;
		case R.id.readSpeed:
			this.onCreateDialog(this.SPEED_DIALOG);
			return true;
		case R.id.chooseFile:
			dialog =ProgressDialog.show(this, "", getString(R.string.loading), true);
			wl = new WordLoader(new File(Environment.getExternalStorageDirectory(), "test.txt"), bobby);
			Thread thread = new Thread(wl);
			thread.start();
			return true;
		default: return false; 
		}
	}

	protected Dialog onCreateDialog(int id)
	{
		int titleID = -1;
		final int ID = id;
		switch(id)
		{
		case BACKWARD_DIALOG:
			titleID = R.string.backward_title;
			break;
		case FORWARD_DIALOG:
			titleID = R.string.forward_title;
			break;
		default:
			titleID = R.string.speed_title;
		}

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(titleID);

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				String s = input.getText().toString();
				int amt = Integer.parseInt(s);

				switch(ID)
				{
				case BACKWARD_DIALOG:
					Reader.this.wl.setBackwardsAmount(amt);
					break;
				case FORWARD_DIALOG:
					Reader.this.wl.setForwardsAmount(amt);
					break;
				default:
					Reader.this.wpm = amt;
				}
			}
		});

		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				// Canceled.
			}
		});

		return alert.show();
	}

	private void pause()
	{
		hand.removeCallbacks(runner);
		play.setImageResource(R.drawable.play);
	}

	private void play()
	{
		hand.post(runner);
		play.setImageResource(R.drawable.pause);
	}

	private void updateWord()
	{
		read.setText(wl.getWord());
	}

	private Handler bobby = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			dialog.dismiss();
		}

	};
}