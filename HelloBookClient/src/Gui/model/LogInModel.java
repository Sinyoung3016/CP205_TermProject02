package Gui.model;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class LogInModel {
	public static String BGM="On";
	public static boolean status_log_outed=true;
	public static File backgroundSoundDirFile = new File(".\\sound\\background");
	public static File[] backgroundSoundFileList = backgroundSoundDirFile.listFiles();
	public static Media b_me=new Media(backgroundSoundFileList[0].toURI().toString());
	
	public static MediaPlayer b_mp=new MediaPlayer(b_me);
}

