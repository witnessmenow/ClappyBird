package com.ladinc.clappybird.core.collision;

import com.ladinc.clappybird.core.objects.Bird;
import com.ladinc.clappybird.core.objects.Pipe;

public class CollisionInfo {

	public String text;
	public CollisionObjectType type;

	public Bird bird;

	public Pipe pipe;


	public CollisionInfo(String text, CollisionObjectType type)
	{
		this.text = text;		
		this.type = type;
	}

	public CollisionInfo(String text, CollisionObjectType type, Pipe pipe)
	{
		this.text = text;		
		this.type = type;
		this.pipe = pipe;
	}

	public CollisionInfo(String text, CollisionObjectType type, Bird bird)
	{
		this.text = text;		
		this.type = type;
		this.bird = bird;
	}

	public static enum CollisionObjectType{Rink, Goal, Player, ScoreZone, Puck};

}
