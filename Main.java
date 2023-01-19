import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static final String savePath = "D:\\Games\\savegames\\";

    public static ArrayList<String> scanSMG() {
        ArrayList<String> filesToZip = new ArrayList<>();
        File dir = new File(savePath);
        if (dir.isDirectory()) {
            for (File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isFile()) {
                    if (item.getName().contains(".smg")) {
                        filesToZip.add(item.getName());
                    }

                }
            }
        }
        return filesToZip;
    }

    public static boolean zipFiles(String zipPathName, ArrayList<String> filesToZip) {

        try (ZipOutputStream zout = new ZipOutputStream(new
                FileOutputStream(savePath + zipPathName))) {

            for (String fileName : filesToZip
            ) {
                FileInputStream fis = new FileInputStream(savePath + fileName);//close
                ZipEntry entry = new ZipEntry(fileName);
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();

            }
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    public static boolean saveGame(GameProgress gameProgress, String fileName) {

        try (FileOutputStream fos = new FileOutputStream(savePath + fileName + ".smg");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage(
            ));
            return false;
        }
    }

    public static boolean deleteFile(ArrayList<String> filesToDelete) throws IOException {
        try {
            for (String fileToDelete : filesToDelete) {
                Path path = Path.of(savePath + fileToDelete);
                Files.delete(path);
            }
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

    }

    public static void main(String[] args) throws IOException {

        GameProgress gamer1 = new GameProgress(100, 5, 80, 54.3);
        GameProgress gamer2 = new GameProgress(100, 5, 80, 54.3);
        GameProgress gamer3 = new GameProgress(100, 5, 80, 54.3);

        System.out.println(saveGame(gamer1, "gamer1") ? "gamer1 сохранён" : "не удалось сохранить gamer1");
        System.out.println(saveGame(gamer2, "gamer2") ? "gamer2 сохранён" : "не удалось сохранить gamer2");
        System.out.println(saveGame(gamer3, "gamer3") ? "gamer3 сохранён" : "не удалось сохранить gamer3");

        ArrayList<String> filesToZip = scanSMG();

        if (filesToZip != null) {
            if (zipFiles("zipMSG.zip", filesToZip)) {
                System.out.println("Файлы успешно добавлены в архив");
                System.out.println(deleteFile(filesToZip) ? "все msg файлы удалены" : "не удалось удалить msg файлы");
            } else {
                System.out.println("Не удалось добавить файлы в архив");
            }
        }

    }
}