/*
	Take a flattened User-Item matrix & convert it to 2D with one user
	per row.
*/
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.math.*;
public class ConvertUITo2D{
	/* Command line args:
		java ConvertUITo2D N DIR INPUT
		N: total number of songs
		DIR: the output directory
		INPUT: the input file
	*/
	public static void main(String[] args){
		int numSongs = Integer.parseInt(args[0]);
		File outDirectory = new File(args[1]);
		File inputFile = new File(args[2]);
		if(!outDirectory.exists() || !outDirectory.isDirectory()){
			System.err.println("Bad output directory: " + args[1]);
			return;
		}
		if(!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead()){
			System.err.println("Bad input file: " + args[2]);
			return;
		}
		int[] curRow = new int[numSongs];
		long curUser = 0;
		try{
			Scanner scan = new Scanner(inputFile);
			PrintWriter out = new PrintWriter(new File(outDirectory+"/UI2Dmatrix"));
			while(scan.hasNext()){
				long tmpUser = scan.nextLong();
				int curSong = scan.nextInt();
				int playCount = scan.nextInt();
				if(curUser != tmpUser){
					out.println(Arrays.toString(curRow));
					curRow = new int[numSongs];
					curUser = tmpUser;
				}
				curRow[curSong] = playCount;
			}
			out.println(Arrays.toString(curRow));
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
