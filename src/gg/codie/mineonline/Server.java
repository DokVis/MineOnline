package gg.codie.mineonline;

import gg.codie.utils.MD5Checksum;

import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception{
        File jarFile = new File(args[0]);
        if(!jarFile.exists()) {
            System.err.println("Couldn't find jar file " + args[0]);
            System.exit(1);
        }

        LibraryManager.extractLibraries();
        LibraryManager.updateClasspath();

        String md5 = MD5Checksum.getMD5ChecksumForFile(jarFile.getPath());
        MinecraftVersion serverVersion = MinecraftVersionRepository.getSingleton().getVersionByMD5(md5);

        if(serverVersion.legacy) {
            LegacyMinecraftServerLauncher.main(args);
        } else {
            MinecraftServerLauncher.main(args);
        }
    }
}
