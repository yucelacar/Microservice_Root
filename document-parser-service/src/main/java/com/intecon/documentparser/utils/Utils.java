/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intecon.documentparser.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;

/**
 *
 * @author SESA251578
 */
public class Utils {

	public static String generateJSAlert(String msg) {
		return "<script>alert('" + msg.replace("'", "\'") + "')</script>";
	}

	public static String getToday() {
		return new java.sql.Date(new java.util.Date().getTime()).toString().substring(0, 10);
	}

	public static void createZipFile(File zip_file, File input_file) {
		ZipOutputStream zos = null;
		try {
			String name = input_file.getName();
			zos = new ZipOutputStream(new FileOutputStream(zip_file));

			ZipEntry entry = new ZipEntry(name);
			zos.putNextEntry(entry);

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(input_file);
				byte[] byteBuffer = new byte[1024];
				int bytesRead = -1;
				while ((bytesRead = fis.read(byteBuffer)) != -1) {
					zos.write(byteBuffer, 0, bytesRead);
				}
				zos.flush();
			} finally {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
			zos.closeEntry();

			zos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static File writeInputStremToFile(InputStream inputStream, String outfilename) {
		File r = new File(outfilename);
		OutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(r);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			if (!r.exists())
				r = null;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return r;
	}
	
	public static void extractZipFile(String zip_file, String out_file) {
		try {

			OutputStream out = new FileOutputStream(out_file);
			ZipEntry ze = null;

			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(zip_file));

			ZipInputStream zin = new ZipInputStream(bin);

			while ((ze = zin.getNextEntry()) != null) {
				byte[] buffer = new byte[8192];
				int len;
				while ((len = zin.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.close();
				break;
			}
			zin.close();
		} catch (Exception e0) {
			e0.printStackTrace();
		}
	}
	
	public static String convertObjectToString(Object object) {
		String response = "";
		Gson gson = null;
		try {
			gson = new Gson();
			response = gson.toJson(object);
		} catch (Exception e) {
			response= "Conversion failed!";
		}finally {
			gson = null;
		}
		return response;
	}

}
