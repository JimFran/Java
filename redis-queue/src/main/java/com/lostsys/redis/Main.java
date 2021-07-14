package com.lostsys.redis;

import java.util.UUID;

import redis.clients.jedis.Jedis;

import java.util.List;

public class Main {
	
	private static String REDIS_HOST="172.17.0.2";
	private static String REDIS_LIST="Array";

	public static void main(String[] args) {
		
		if ( args.length!=1 ) {
			printUsage();		
		} else if ( args[0].contentEquals("producer") ) {
			producer();
		} else if ( args[0].equals("consumer") ) {
			consumer();
		} else { 
	    	printUsage();
        }
	}
	
	
	public static void producer() {
		Jedis jedis = new Jedis( REDIS_HOST );
		
		while (true) {
			
			String newElement=UUID.randomUUID().toString();
			jedis.rpush( REDIS_LIST, newElement );
			
			/* Clean the screen */
			
			System.out.print("\0333[H<033[23");
	        System.out.flush();
	        
	        System.out.println("REDIS Producer");
	        System.out.println("+++++++++++++");
	        System.out.println("");
	        
	        System.out.println("Total of items: " +jedis.llen(REDIS_LIST));
	        System.out.println("Item: "+jedis.lrange( REDIS_LIST, 0, -1 ));
	        
	        try {
	        	Thread.sleep(1000);
	        } catch (Exception ex) { ex.printStackTrace(); }
	        
			
		}
		
	}
	
	public static void consumer() {
		Jedis jedis = new Jedis( REDIS_HOST );
		
		while (true) {
			
			System.out.print("\0333[H<033[23");
	        System.out.flush();
	        
	        System.out.println("REDIS Consumer");
	        System.out.println("+++++++++++++");
	        System.out.println("");
	        
	        List<String> element=jedis.blpop( 0, REDIS_LIST );
	        
	        System.out.println("Read item: "+element );
	        System.out.println(" ("+jedis.llen( REDIS_LIST )+" Pednings) ");
	        
	        try {
	        	Thread.sleep(1500);
	        } catch (Exception ex) { ex.printStackTrace(); }
			
		}
	}
	
	public static void printUsage() {
		System.out.println("");
		System.out.println("USAGE: java -jar redis-queue <producer|consumer>");
		System.out.println("");
	}

}
