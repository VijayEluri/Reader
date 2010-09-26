package com.worldsproject.reader.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;

public class WordLoader implements Runnable
{
	private File file = null;
	private int location = -1;

	private int forwardsAmount = 10;
	private int backwardsAmount = 10;

	private ArrayList<String> words = new ArrayList<String>(4);
	
	private Handler hand = null;

	public WordLoader()
	{
		words.add("one");
		words.add("two");
		words.add("three");
		words.add("four");
		words.add("five");
		words.add("six");
	}

	public WordLoader(File f, Handler h)
	{
		file = f;
		hand = h;
	}
	
	public void run() 
	{
		if(file != null)
		{
//			words.clear();
			
			try 
			{
				BufferedReader buf = new BufferedReader(new FileReader(file));

				String line = null;


				while((line = buf.readLine()) != null)
				{
					String[] temp = line.split("\\s");

					for(String t : temp)
					{
						t.trim();
						
						if(t.length() > 0)
							words.add(t);
					}
				}
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		hand.dispatchMessage(new Message());
	}

	public void nextWord()
	{	
		location++;

		checkLocation();
	}

	public String getWord()
	{
		checkLocation();
		return words.get(location);
	}

	public void moveForward()
	{
		location += forwardsAmount;
		checkLocation();
	}

	public void moveBackward()
	{
		location -= backwardsAmount;
		checkLocation();
	}

	public void setBackwardsAmount(int amount)
	{
		backwardsAmount = amount;
	}

	public void setForwardsAmount(int amount)
	{
		forwardsAmount = amount;
	}

	private void checkLocation()
	{
		if(location < 0)
			location = 0;

		if(location >= words.size())
			location = words.size() - 1;
	}
}
