package com.worldsproject.reader.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordLoader
{
	private File file = null;
	private String[] defaultText = {"Ready", "one", "two","three", "four", "five", "six", "seven", "eight", "nine", "ten", "elevin", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty"};
	private int location = -1;

	private int forwardsAmount = 10;
	private int backwardsAmount = 10;

	private ArrayList<String> words = new ArrayList<String>();

	public WordLoader()
	{

	}

	public WordLoader(File f)
	{
		if(f!= null)
		{
			try 
			{
				BufferedReader buf = new BufferedReader(new FileReader(f));

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
		else
		{
			for(String s : defaultText)
			{
				words.add(s);
			}
		}
	}

	public void nextWord()
	{	
		location++;

		checkLocation();
	}

	public String getWord()
	{
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
