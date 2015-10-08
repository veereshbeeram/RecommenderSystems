/*
	This is a standalone class to convert the triples given in Million song
	data-set into a user-item matrix. This gives us data ready for standard
	CF algorithmic attack.
	TODO:
		* Logging
		* Better error & exception handling.
*/
import java.util.*;
import java.io.*;
import java.math.*;
import java.util.regex.*;
public class ConvertMSDToUIMatrix{
	/*
		Main! Takes a Directory & list of filenames 
				(each having disjoint-set of users)
		and prints out a flattened user-item matrix.
		The output will have 3 space seperated numbers: i, j, Wij
			where i: ith user
			      j: jth song
			    Wij: the number of times ith user listened to jth song
		Also generates a userIndex mapping userid to integer
			and a songIndex mapping SongId to integer.
	*/
	public static void main(String[] args){
		long curUserIndex = 0; //user indexes start from zero
		String curUser = ""; // the hash of cur user.
		long curSong = 0; //song too starts from zero
		// store all the songs seen so far in hashmap;
		// TODO: does this cause huge memory issues?
		Map<String, Long> songMap = new HashMap<String, Long>();
		// No map needed for users, since users are sorted in a given file &
		// 	are non-repeating across files. A characterstic of MSD data.
		PrintWriter outUser = null;
		PrintWriter outSong = null;
		for(String fileName: args){
			File curInputFile = new File(fileName);
			if(curInputFile.exists() && curInputFile.isDirectory()){
				try{
					outUser = new PrintWriter(new File(fileName + "userIndex"));
					outSong = new PrintWriter(new File(fileName + "songIndex"));
				}catch(Exception e){
					e.printStackTrace();
				}
				continue;
			}
			if(!curInputFile.exists() || !curInputFile.canRead()){
				System.err.println("can't read the file: " + fileName);
				break;
			}
			File curOutputFile = new File(fileName + ".UIMatrix");
			try{
				Scanner scan = new Scanner(curInputFile);
				PrintWriter out = new PrintWriter(curOutputFile);
				while(scan.hasNext()){
					String tmpUser = scan.next();
					String tmpSong = scan.next();
					long playCount = scan.nextLong();
					scan.nextLine();
					if(curUser.length() == 0)
						curUser = tmpUser;
					if(!curUser.equals(tmpUser)){
						outUser.format("%d %s\n", curUserIndex, curUser);
						curUser = tmpUser;
						curUserIndex++;
					}
					if(!songMap.containsKey(tmpSong)){
						outSong.format("%d %s\n", curSong, tmpSong);
						songMap.put(tmpSong, curSong);
						curSong++;
					}
					out.format("%d %d %d\n", curUserIndex, songMap.get(tmpSong), playCount);
				}
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		outUser.flush(); outUser.close();
		outSong.flush(); outSong.close();
	}
}
