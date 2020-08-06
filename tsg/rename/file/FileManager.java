package tsg.rename.file;

import java.io.File;
import java.io.IOException;

import tsg.rename.main.TSGRename;

public class FileManager {

	public File saveList = new File(TSGRename.instance.getDataFolder(), "save.data");

	public void setup() {
		TSGRename.instance.saveDefaultConfig();
		if (!saveList.exists()) {
			try {
				saveList.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
