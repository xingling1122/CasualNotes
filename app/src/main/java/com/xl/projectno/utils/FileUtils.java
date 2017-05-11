package com.xl.projectno.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 *
 * 
 * 文件工具类
 */
public class FileUtils {

	static public boolean writeSerializeFile(String filename, Context context, Object obj) {
		FileOutputStream ostream = null;
		try {
			ostream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		try {
			ObjectOutputStream p = new ObjectOutputStream(ostream);
			p.writeObject(obj);
			p.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (ostream != null) {
				try {
					ostream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	static public Object readSerializeFile(String filename, Context context) {
		FileInputStream istream = null;
		try {
			istream = context.openFileInput(filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		ObjectInputStream q;
		try {
			q = new ObjectInputStream(istream);
			return q.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static boolean isFileExists(String path) {
		try {
			return new File(path).exists();
		} catch (Exception localException) {
			localException.printStackTrace();
			return false;
		}
	}

	public static String saveBitmap(Bitmap bitmap){
		if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
		{    // 获取SDCard指定目录下
			String sdCardDir = Environment.getExternalStorageDirectory()+ "/CasualNotes/";
			String fileName = "user.jpg";
			File dirFile  = new File(sdCardDir);
			if (!dirFile .exists()) {
				dirFile.mkdirs();
			}

			File file = new File(sdCardDir, fileName);
			if (file.exists()){
				file.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
			return sdCardDir + fileName;
		}else {
			return null;
		}
	}

	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {

				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];


				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}else {
                    /*可能位于外置SD卡中,直接提交所有可能的目录,逐个尝试*/
					String properly[] = getAllSdPath(context);
					String results = null;
					if (properly != null){
						for (String s : properly){
							results = s + "/" + split[1];
							if (isFileExists(results)){
								System.out.println("成功获取目录----" + results);
								return results;
							}
						}
					}

					return results;
				}


				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];


				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	private static String[] getAllSdPath(Context context){
		String[] paths = null;
		StorageManager manager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		try {
			Method methodGetPaths = manager.getClass().getMethod("getVolumePaths");
			paths = (String[]) methodGetPaths.invoke(manager);
			return paths;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] openFile(String name) {
		if (isFileExists(name) == false) {
			return null;
		}
		FileInputStream is = null;
		try {
			is = new FileInputStream(name);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			return buffer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean saveFile(String name, byte[] file) {
		if (file != null) {
			try {
				FileOutputStream is = new FileOutputStream(name);
				is.write(file);
				is.flush();
				is.close();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}


	public static boolean createFolder(String path) {
		File folder = new File(path);
		if ((folder.exists()))
			return true;
		else {
			synchronized (FileUtils.class) {
				return folder.mkdirs();
			}
		}
	}

	/**
	 * <br>功能简述:根据文件路径删除文件
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static boolean saveByteToSDFile(byte[] byteData, String absolutefilePath) {
		boolean result = false;
		FileOutputStream fileOutputStream = null;
		File newFile = null;
		try {
			newFile = new File(absolutefilePath);
			if (!newFile.exists()) {
				File parent = newFile.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				newFile.createNewFile();
			}
			fileOutputStream = new FileOutputStream(newFile);
			fileOutputStream.write(byteData);
			fileOutputStream.flush();
			result = true;
		} catch (Exception e) {
			if (newFile != null && newFile.exists()) {
				newFile.delete();
			}
			e.printStackTrace();

		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static byte[] readFileToByte(String filePath) throws Exception {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		InputStream inputStream = null;
		inputStream = new FileInputStream(file);
		byte[] data = toByteArray(inputStream);
		if (inputStream != null) {
			inputStream.close();
		}
		return data;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static byte[] getByteFromSDFile(String absolutefilePath) {
		byte[] bs = null;
		FileInputStream fileInputStream = null;
		try {
			File newFile = new File(absolutefilePath);
			fileInputStream = new FileInputStream(newFile);
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			BufferedInputStream inPutStream = new BufferedInputStream(dataInputStream);
			bs = new byte[(int) newFile.length()];
			inPutStream.read(bs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bs;
	}

	//判断sd卡是否存在
	public static boolean isSDCardAvaiable() {
		String state = Environment.getExternalStorageState();
		if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static void deleteDirectory(File f) {
		if (f.exists() && f.isDirectory()) {
			File delFile[] = f.listFiles();
			if (delFile != null) {
				int length = delFile.length;
				//判断是文件还是目录
				if (length == 0) {
					//若目录下没有文件则直接删除
					f.delete();
				} else {
					//若有则把文件放进数组，并判断是否有下级目录
					for (int j = 0; j < length; j++) {
						if (delFile[j].isDirectory()) {
							deleteDirectory(delFile[j]); //递归调用del方法并取得子目录路径
						}
						delFile[j].delete(); //删除文件
					}
				}
			}
		}
	}

	public static Properties loadConfig(Context context, String file) {
		Properties properties = new Properties();
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return properties;
	}

	public static boolean saveConfig(Context context, String file, Properties properties) {
		try {
			File fil = new File(file);
			if (!fil.exists())
				fil.createNewFile();
			FileOutputStream s = new FileOutputStream(fil);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void changeMod(String fileName){
		try{
			String command = "chmod 777 "+fileName;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		}catch (Exception e){

		}
	}

}
