package com.imageServer.imageServer.controller;

import java.awt.image.BufferedImage;
import com.imageServer.imageServer.repository.ImageServerRepo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;
import com.imageServer.imageServer.model.Images;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageServerController {
	@Autowired
	private ImageServerRepo imageServerRepo;
	Images images = new Images();

	@GetMapping(value = "/ping")
	public String Success() {
		return "success";
	}

	// File representing the folder that you select using a FileChooser
	static final File dir = new File("C:\\Users\\Stephen\\Pictures\\Camera Roll");

	// array of supported extensions (use a List if you prefer)
	static final String[] EXTENSIONS = new String[] { "gif", "png", "bmp" // and other formats you need
	};
	// filter to identify images based on their extensions
	static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

		@Override
		public boolean accept(final File dir, final String name) {
			for (final String ext : EXTENSIONS) {
				if (name.endsWith("." + ext)) {
					return (true);
				}
			}
			return (false);
		}
	};

	@RequestMapping(value = "/showImageDetails")
	public ResponseEntity<?> DisplayImage() {

		if (dir.isDirectory()) { // make sure it's a directory
			for (final File f : dir.listFiles(IMAGE_FILTER)) {
				BufferedImage img = null;

				try {
					img = ImageIO.read(f);

					// you probably want something more involved here
					// to display in your UI
					System.out.println("image: " + f.getName());
					System.out.println(" width : " + img.getWidth());
					System.out.println(" height: " + img.getHeight());
					System.out.println(" size  : " + f.length());

					// f.toArray();
					String encodedImageString = encodeFileToBase64Binary(f);
					System.out.println(encodedImageString);
//					ArrayList <Images> list = new ArrayList <> ();
//					list.add(images);
					images.setImageData(encodedImageString);
					imageServerRepo.save(images);
					// images.add(Base64.getEncoder().encodeToString(img.toArray()));
					// imageServerRepo.save(images);
				} catch (final IOException e) {
					// handle errors here
				}
			}
		}
		return new ResponseEntity<>(IMAGE_FILTER, HttpStatus.OK);
	}

//	public ResponseEntity<?> SaveImage() {
//		// MultipartFile[] images = bindingModel.getImages();
//	}

	private static String encodeFileToBase64Binary(File file) {
		String encodedfile = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fileInputStreamReader.close();
		return encodedfile;
	}

}