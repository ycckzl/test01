package com.main.base.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileUtils {
	
	
	 /**
     * 获取目录下所有文件(按时间排序)
     * 
     * @param path
     * @return
     */
    public static List<File> getFileSort(String path) {
 
        List<File> list = getFiles(path, new ArrayList<File>());
 
        if (list != null && list.size() > 0) {
 
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }
 
                }
            });
 
        }
 
        return list;
    }
    
    
    /**
     * 
     * 获取目录下所有文件
     * 
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {
 
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }
 

		/**保存文件
	     * @param stream
	     * @param path
	     * @param filename
	     * @throws IOException
	     */
		public static void saveFileFromInputStream(InputStream stream,String path,String filename) throws IOException{      
	        FileOutputStream fs=new FileOutputStream( path + "/"+ filename);
	        byte[] buffer =new byte[1024*1024];
	        int bytesum = 0;
	        int byteread = 0; 
	        while ((byteread=stream.read(buffer))!=-1)
	        {
	           bytesum+=byteread;
	           fs.write(buffer,0,byteread);
	           fs.flush();
	        } 
	        fs.close();
	        stream.close();      
	    }
		
		/** 
		* @author liulina 
		* @date 2015-8-5 上午10:42:27
		* @Description:将内容追加入文件
		* @param file
		* @param content 
		* @throws 
		*/ 
		public static void append(String file,String content){
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
				out.write(content);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public static void stringToFile(String str,String filename){
			   try 
			   {
			     BufferedReader in = new BufferedReader(new StringReader(str));

			     PrintWriter out = new PrintWriter(new FileWriter(filename));

			     String s;

			    while ((s = in.readLine()) != null){
			      out.println(s);
			    }

			     out.close();

			   }catch (IOException e4) {
				   e4.printStackTrace();
			   }
			   
	   }
		public static void saveFileFromInputStream(InputStream stream,String filename) throws IOException{      
	        FileOutputStream fs=new FileOutputStream( filename);
	        byte[] buffer =new byte[1024*1024];
	        int bytesum = 0;
	        int byteread = 0; 
	        while ((byteread=stream.read(buffer))!=-1)
	        {
	           bytesum+=byteread;
	           fs.write(buffer,0,byteread);
	           fs.flush();
	        } 
	        fs.close();
	        stream.close();      
	    }
		/***
		 * 
		 * 2011-7-27, 下午03:00:46
		 * 方法描述：下载文件
		 * @param filepath
		 * @param filename
		 * @param response
		 */
		public static void finddownloadfile(String filepath,String filename,HttpServletResponse response)
		{
			OutputStream outputStream = null;
			
			response.setContentType("application/data;charset=UTF-8");
			String filePath1 = filepath+File.separator+filename;
			File file = new File(filePath1);
			try {
	
			FileInputStream fis = null;
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
		
				outputStream = response.getOutputStream();
			
				if (file.exists()) {
					response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gb2312"), "ISO8859-1" ));
					response.setHeader("Content-Length", ""+file.length());  
					fis = new FileInputStream(file);
					while ((bytesRead = fis.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					fis.close();
					outputStream.close();
				}else{
					
				}
			} catch (Exception ee) {
				System.out.println("ee == " + ee.getLocalizedMessage());
				ee.printStackTrace();
			}
		}
		
		 public static String readFileByLines(String fileName) {
		        File file = new File(fileName);
		        BufferedReader reader = null;
		        String reStr = "";
		        try {
		            System.out.println("以行为单位读取文件内容，一次读一整行：");
		            reader = new BufferedReader(new FileReader(file));
		            String tempString = null;
		            int line = 1;
		            // 一次读入一行，直到读入null为文件结束
		            while ((tempString = reader.readLine()) != null) {
		                // 显示行号
		                System.out.println("line " + line + ": " + tempString);
		                reStr +=tempString+"\r\n";
		                line++;
		            }
		            reader.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally {
		            if (reader != null) {
		                try {
		                    reader.close();
		                } catch (IOException e1) {
		                }
		            }
		        }
		        return reStr;
		    }
		
	    public static String readFile(String fileName) {  
	    	String output = "";   
	    	try{
		          
		        File file = new File(fileName);  
		             
		        if(file.exists()){  
		            if(file.isFile()){  
		                try{  
		                    BufferedReader input = new BufferedReader (new FileReader(file));  
		                    StringBuffer buffer = new StringBuffer();  
		                    String text;  
		                         
		                    while((text = input.readLine()) != null)  
		                        buffer.append(text).append("\r\n");  
		                    output = buffer.toString();                      
		                }  
		                catch(IOException ioException){  
		                    System.err.println("File Error!");  
		                }  
		            }  
		            else if(file.isDirectory()){  
		                String[] dir = file.list();  
		                output += "Directory contents:/n";  
		                  
		                for(int i=0; i<dir.length; i++){  
		                    output += dir[i] +"/n";  
		                }  
		            }  
		        }  
		        else{  
		            System.err.println("Does not exist!");  
		        }  
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	return output;  
		     } 
	    public static byte[] readBinaryFile(String fileName) throws Exception {  
	    	try{
		          
		        File file = new File(fileName);  
		             
		        if(file.exists()){  
		            if(file.isFile()){  
		                try{  
		                	FileInputStream fis = new FileInputStream(fileName); 
		                	byte[] outByte = InputStreamUtils.InputStreamTOString(fis);
		                	return outByte;
		                }  
		                catch(IOException ioException){  
		                    System.err.println("File Error!");  
		                }  
		            }  
		            else if(file.isDirectory()){  
		                String[] dir = file.list();  
		               String  output = "Directory contents:/n";  
		                  
		                for(int i=0; i<dir.length; i++){  
		                    output += dir[i] +"/n";  
		                } 
		                System.out.println(output);
		            }  
		        }  
		        else{  
		        	System.err.println("Does not exist!");  
		        	throw new Exception("文件不存在!");
		        }  
		        
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		throw e;
	    	}
	    	return null;  
		     } 
	    
	    /**
	     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	     */
	    public static String  readFileByBytes(String fileName) {
	    	String output = "";
	        File file = new File(fileName);
	        InputStream in = null;
	        try {
	            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
	            // 一次读一个字节
	            in = new FileInputStream(file);
	            int tempbyte;
	            while ((tempbyte = in.read()) != -1) {
	                //System.out.write(tempbyte);
	            	output = output+tempbyte;
	            }
	            in.close();
	           System.out.println("len == " + output.length());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	       /* try {
	            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
	            // 一次读多个字节
	            byte[] tempbytes = new byte[100];
	            int byteread = 0;
	            in = new FileInputStream(fileName);
	            showAvailableBytes(in);
	            // 读入多个字节到字节数组中，byteread为一次读入的字节数
	            while ((byteread = in.read(tempbytes)) != -1) {
	                System.out.write(tempbytes, 0, byteread);
	            }
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e1) {
	                }
	            }
	        }*/
	        return output;
	    }
	    
	    /**
	     * 显示输入流中还剩的字节数
	     */
	    private static void showAvailableBytes(InputStream in) {
	        try {
	            System.out.println("当前字节输入流中的字节数为:" + in.available());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	       public static String stringToAscii(String value)
	     	{
	     		StringBuffer sbu = new StringBuffer();
	     		char[] chars = value.toCharArray(); 
	     		for (int i = 0; i < chars.length; i++) {
	     			if(i != chars.length - 1)
	     			{
	     				sbu.append((int)chars[i]).append(",");
	     			}
	     			else {
	     				sbu.append((int)chars[i]);
	     			}
	     		}
	     		return sbu.toString();
	     	}
		
		/**
		 * 递归查询目录下所有文件名以beginName开始的文件
		 * @param returnList 
		 * @param name（）
		 * @param beginName
		 * @return
		 */
		public static List filelist(List returnList, String name, String beginName) {
			try {
				File f = new File(name);
				if (!f.exists()) {
					System.out.println("文件不存在哦");
					return returnList;
				}

				if (f.isDirectory()) {

					 File[] list = f.listFiles(getFileBeginFilter(beginName));
//					File[] list = f.listFiles(getFileExtensionFilter(beginName));
					// System.out.println(list.length);
					for (int i = 0; i < list.length; i++)
						returnList.add(list[i]);
					list = f.listFiles(getNotDirFileFilter());
					for (int i = 0; i < list.length; i++)
						if (list[i].isDirectory())
							filelist(returnList, list[i].toString(), beginName);
				}

			} catch (Exception e) {
				System.out.println("IO error!\r\n" + e.toString());
			}
			return returnList;
		}



		/** 
		* @author liulina 
		* @date 2012-2-15 下午12:58:19
		* @Description:指定扩展名过滤
		* @param extension
		* @return 
		* @throws 
		*/ 
		public static FilenameFilter getFileExtensionFilter(String extension) {// 指定扩展名过滤
			final String _extension = extension;
			return new FilenameFilter() {
				public boolean accept(File file, String name) {
					boolean ret = name.endsWith(_extension);
					return ret;
				}
			};
		}

		/** 
		* @author liulina 
		* @date 2012-2-15 下午12:58:40
		* @Description: 指定开始文件名过滤
		* @param beinStr
		* @return 
		* @throws 
		*/ 
		public static FilenameFilter getFileBeginFilter(String beinStr) {// 指定开始文件名过滤
			final String _extension = beinStr;
			return new FilenameFilter() {
				public boolean accept(File file, String name) {
					boolean ret = name.startsWith(_extension);
					return ret;
				}
			};
		}

		/** 
		* @author liulina 
		* @date 2012-2-15 下午12:59:01
		* @Description:文件还是目录过滤
		* @return 
		* @throws 
		*/ 
		public static FileFilter getNotDirFileFilter() { // 文件还是目录过滤

			return new FileFilter() {

				public boolean accept(File file) {

					return file.isDirectory();// 关键判断点

				}

			};

		}
		

	  
		/**
		 * @Create_by:liu lina
		 * 2011-9-19, 下午02:08:14
		 *
		 * 方法描述：读取文件生成String 
		 * @param fileName
		 * @return
		 */
		public static String selfReadFile(String fileName) {
			StringBuffer buf = null;// the intermediary, mutable buffer
			BufferedReader breader = null;// reader for the template files
			try {
				breader = new BufferedReader(
				        new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
				buf = new StringBuffer();
				while (breader.ready())
					buf.append((char) breader.read());
				breader.close();
			}// try
			catch (Exception e) {
				e.printStackTrace();
			}// catch
			return buf.toString();
		}
		
	/*	  public static Object xmlToJavaBean(String filename){
			  XStream stream = new XStream();
//			   String xml = "d://virtual/authorizedFile.xml";  
			   try {   
			    stream.alias("AuthProduct",AuthProduct.class);
			    stream.alias("ProductInfo",ProductInfoBean.class); 
			    stream.alias("IssueInfo",IssueInfoBean.class); 
			    stream.alias("AuthList",AuthListBean.class); 
			   //设置隐含转化集合

			    //根节点books对应Books类，book节点对应Book类，有多个book节点，这里需转换到集合中
			   // stream.addImplicitCollection(AuthProduct.class, "AuthProduct");   
			    AuthProduct auth = (AuthProduct) stream.fromXML(new FileReader(new File(filename)));
			    return auth;
			    
			   } catch (FileNotFoundException e) {   
				    e.printStackTrace();
		       }
			   return null;
		  }*/
		  //根据上传的base64后的串，直接生成zip文件
		  public static String zipFile(byte[] data,String fileName) throws IOException{
				//File zipFile = new File(fileName);
				/*ByteArrayOutputStream bos = new ByteArrayOutputStream();
				   ZipOutputStream zip = new ZipOutputStream(bos);
				   ZipEntry entry = new ZipEntry(fileName);
				   entry.setSize(data.length);
				   try {
					zip.putNextEntry(entry);
				
					   zip.write(data);
					   zip.closeEntry();
					   zip.close();
				   } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				 FileOutputStream fs=new FileOutputStream(fileName);
			     fs.write(data);
			     fs.close();
				return null;
			}
		  
		  public static List unZipFile(String fileName) {
			    List list = new ArrayList();
				try{
					int BUFFER = 1024;
					BufferedOutputStream dest = null;
			        FileInputStream fis = new FileInputStream(fileName);
			        String dirPath = fileName.substring(0,fileName.lastIndexOf(File.separator));
			        System.out.println(dirPath);
			        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			        ZipEntry entry;
			        while ((entry = zis.getNextEntry()) != null) {
			            System.out.println("Extracting: " + entry);
			            list.add(entry.getName());
			            int count;
			            byte data[] = new byte[1024];
			            FileOutputStream fos = new  FileOutputStream(dirPath+File.separator+entry.getName());
			            dest = new BufferedOutputStream(fos, BUFFER);
			            while ((count = zis.read(data, 0, BUFFER)) != -1) {
			                dest.write(data, 0, count);
			            }
			            dest.flush();
			            dest.close();
			        }
			        zis.close();
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
                   return list;
				}
		  
		    /**
		     * 删除目录（文件夹）以及目录下的文件
		     * @param   sPath 被删除目录的文件路径
		     * @return  目录删除成功返回true，否则返回false
		     */
		    public static boolean deleteDirectory(String sPath) {
		        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
		        if (!sPath.endsWith(File.separator)) {
		            sPath = sPath + File.separator;
		        }
		        File dirFile = new File(sPath);
		        //如果dir对应的文件不存在，或者不是一个目录，则退出
		        if (!dirFile.exists() || !dirFile.isDirectory()) {
		            return false;
		        }
		        boolean flag = true;
		        //删除文件夹下的所有文件(包括子目录)
		        File[] files = dirFile.listFiles();
		        for (int i = 0; i < files.length; i++) {
		            //删除子文件
		            if (files[i].isFile()) {
		                flag = deleteFile(files[i].getAbsolutePath());
		                if (!flag) break;
		            } //删除子目录
		            else {
		                flag = deleteDirectory(files[i].getAbsolutePath());
		                if (!flag) break;
		            }
		        }
		        if (!flag) return false;
		        //删除当前目录
		        if (dirFile.delete()) {
		            return true;
		        } else {
		            return false;
		        }
		    }
		    
		    /**
		     * 删除单个文件
		     * @param   sPath    被删除文件的文件名
		     * @return 单个文件删除成功返回true，否则返回false
		     */
		    public static boolean deleteFile(String sPath) {
		        boolean flag = false;
		        File file = new File(sPath);
		        // 路径为文件且不为空则进行删除
		        if (file.isFile() && file.exists()) {
		            file.delete();
		            flag = true;
		        }
		        return flag;
		    }
		    
		   /* *//**
			 * 读取Excel表格内容，生成纯文本
			 * @param is 输入流
			 * @return 返回文本字符串
			 * @throws IOException 抛出IO异常
			 *//*
			@SuppressWarnings("deprecation")
			public static String extractTextFromXLS(InputStream is)  throws IOException {    
				    StringBuffer content  = new StringBuffer();    
				    HSSFWorkbook workbook = new HSSFWorkbook(is); //创建对Excel工作簿文件的引用     
		
				    for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {    
				        if (null != workbook.getSheetAt(numSheets)) {    
				            HSSFSheet aSheet = workbook.getSheetAt(numSheets); //获得一个sheet    
		
				            for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {    
				                if (null != aSheet.getRow(rowNumOfSheet)) {    
				                    HSSFRow aRow = aSheet.getRow(rowNumOfSheet); //获得一行    
		
				                    for (short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {    
				                        if (null != aRow.getCell(cellNumOfRow)) {    
				                            HSSFCell aCell = aRow.getCell(cellNumOfRow); //获得列值    
				                                                                
				                            if(aCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){    
				                             content.append(aCell.getNumericCellValue());    
				                            }else if(aCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){    
				                             content.append(aCell.getBooleanCellValue());    
				                            }else {    
				                             content.append(aCell.getStringCellValue());    
				                            }    
				                        }    
				                    }    
				                }    
				            }    
				        }  
				       
				    }    
		
				    return content.toString();	
				
			}*/
		    
		    public static void downFile(HttpServletResponse response, String fileName,
					InputStream stream) {
				OutputStream outputStream = null;

				response.setContentType("application/data;charset=UTF-8");

				byte[] buffer = new byte[4096];
				int bytesRead = 0;
				try {
					outputStream = response.getOutputStream();
				} catch (IOException e) {

					e.printStackTrace();
				}
				try {
					response.setHeader("Content-Disposition", "attachment;filename="
							+ new String(fileName.getBytes("gb2312"), "ISO8859-1"));
					// response.setHeader("Content-Length", ""+file.length());
					while ((bytesRead = stream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					stream.close();
					outputStream.close();

				} catch (Exception ee) {
				}
			}
		    
		    
		    /** 
		     * 复制单个文件 
		     * @param oldPath String 原文件路径 如：c:/fqf.txt 
		     * @param newPath String 复制后路径 如：f:/fqf.txt 
		     * @return boolean 
		     */ 
		/*   public static void copyFile(String oldPath, String newPath) { 
		       try { 
		           int bytesum = 0; 
		           int byteread = 0; 
		           File oldfile = new File(oldPath); 
		           if (oldfile.exists()) { //文件存在时 
		        	   File newFile = new File(newPath);
		               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
		               
		               FileOutputStream fs = new FileOutputStream(newPath); 
		               byte[] buffer = new byte[1444]; 
		               int length; 
		               while ( (byteread = inStream.read(buffer)) != -1) { 
		                   bytesum += byteread; //字节数 文件大小 
		                   System.out.println(bytesum); 
		                   fs.write(buffer, 0, byteread); 
		               } 
		               inStream.close(); 
		           } 
		       } 
		       catch (Exception e) { 
		           System.out.println("复制单个文件操作出错"); 
		           e.printStackTrace(); 
		       } 
		   } */
		   
		   public static void copyFile(String oldPath, String newPath) { 
		       try { 
		           int bytesum = 0; 
		           int byteread = 0; 
		           File oldfile = new File(oldPath); 
		           if (oldfile.exists()) { //文件存在时 
		        	   File newFile = createFile(newPath);
		        	   copyFile(oldfile,newFile);
		           } 
		       } 
		       catch (Exception e) { 
		           System.out.println("复制单个文件操作出错"); 
		           e.printStackTrace(); 
		       } 
		   } 
		   
		// 复制文件
		    public static void copyFile(File sourceFile, File targetFile) throws IOException {
		        BufferedInputStream inBuff = null;
		        BufferedOutputStream outBuff = null;
		        try {
		            // 新建文件输入流并对它进行缓冲
		            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

		            // 新建文件输出流并对它进行缓冲
		            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

		            // 缓冲数组
		            byte[] b = new byte[1024 * 5];
		            int len;
		            while ((len = inBuff.read(b)) != -1) {
		                outBuff.write(b, 0, len);
		            }
		            // 刷新此缓冲的输出流
		            outBuff.flush();
		        } finally {
		            // 关闭流
		            if (inBuff != null)
		                inBuff.close();
		            if (outBuff != null)
		                outBuff.close();
		        }
		    }
		    
		    public static File createFile(String destFileName) {  
		        File file = new File(destFileName);  
		        if(file.exists()) {  
		            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");  
		           // return false;  
		        }  
		        if (destFileName.endsWith(File.separator)) {  
		            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  
		           // return false;  
		        }  
		        //判断目标文件所在的目录是否存在  
		        if(!file.getParentFile().exists()) {  
		            //如果目标文件所在的目录不存在，则创建父目录  
		            System.out.println("目标文件所在目录不存在，准备创建它！");  
		            if(!file.getParentFile().mkdirs()) {  
		                System.out.println("创建目标文件所在目录失败！");  
		               // return false;  
		            }  
		        }  
		        //创建目标文件  
		        try {  
		            if (file.createNewFile()) {  
		                System.out.println("创建单个文件" + destFileName + "成功！");  
		               // return true;  
		            } else {  
		                System.out.println("创建单个文件" + destFileName + "失败！");  
		              //  return false;  
		            }  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());  
		           // return false;  
		        }  
		        return file;
		    }  
		   /** 
		     * 复制整个文件夹内容 
		     * @param oldPath String 原文件路径 如：c:/fqf 
		     * @param newPath String 复制后路径 如：f:/fqf/ff 
		     * @return boolean 
		     */ 
		   public static void copyFolder(String oldPath, String newPath) { 
		       try { 
		           (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
		           File a=new File(oldPath); 
		           String[] file=a.list(); 
		           File temp=null; 
		           for (int i = 0; i < file.length; i++) { 
		               if(oldPath.endsWith(File.separator)){ 
		                   temp=new File(oldPath+file[i]); 
		               } 
		               else{ 
		                   temp=new File(oldPath+File.separator+file[i]); 
		               } 
		               if(temp.isFile()){ 
		                   FileInputStream input = new FileInputStream(temp); 
		                   FileOutputStream output = new FileOutputStream(newPath + File.separator + new String(temp.getName().getBytes("utf-8"), "gbk")); 
		                   byte[] b = new byte[1024 * 5]; 
		                   int len; 
		                   while ( (len = input.read(b)) != -1) { 
		                       output.write(b, 0, len); 
		                   } 
		                   output.flush(); 
		                   output.close(); 
		                   input.close(); 
		               } 
		               if(temp.isDirectory()){//如果是子文件夹 
		                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
		               } 
		           } 
		       } 
		       catch (Exception e) { 
		           System.out.println("复制整个文件夹内容操作出错"); 
		           e.printStackTrace(); 
		       } 
		   }
		   
		   
		   
		   /**//* 
		    * inputFileName 输入一个文件夹 
		    * zipFileName 输出一个压缩文件夹 
		    */  
		    public static void zip(String zipFileName,String inputFileName) throws Exception {  
		       // String zipFileName = "c://test.zip"; //打包后文件名字  
		        System.out.println(zipFileName);  
		        zip(zipFileName, new File(inputFileName));  
		    }  
		  
		    private static  void zip(String zipFileName, File inputFile) throws Exception {  
		        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));  
		        zip(out, inputFile, "");  
		        System.out.println("zip done");  
		        out.close();  
		    }  
		  
		    private static void zip(ZipOutputStream out, File f, String base) throws Exception {  
		        if (f.isDirectory()) {  
		           File[] fl = f.listFiles();  
		           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));  
		           base = base.length() == 0 ? "" : base + "/";  
		           for (int i = 0; i < fl.length; i++) {  
		           zip(out, fl[i], base + fl[i].getName());  
		         }  
		        }else {  
		           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));  
		           FileInputStream in = new FileInputStream(f);  
		           int b;  
		           System.out.println(base);  
		           while ( (b = in.read()) != -1) {  
		            out.write(b);  
		         }  
		         in.close();  
		       }  
		    }  
		    
		    
		   
		    
		    
		    
		    
			public static void main(String[] args) {
				// list("c:\\windows");

				/*List list = new ArrayList();
				list= filelist(list, "F:\\demode\\请制作附件中的任务\\要完成的代码", "access.20101015");
				System.out.println(list.size());
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					String element = iter.next().toString();
					System.out.println(element);
				}*/
				
				//copyFile("d:\\var\\lib\\temp.sql","d://work//var//tem//1.sql");
				//copyFolder("d:\\var","D:\\work\\var");
				
				/*try {
					File enclosureDir = new File("d://work//var//tem.txt");
					if (!enclosureDir.exists()) {
						enclosureDir.mkdir();
					}
					//zip("d:\\var.zip","d:\\var");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				//deleteFile("D:\\var\\lib\\tomcat7\\sysauth\\restore\\alarmFile");
				
				
				
				
				
				//String srcFile="D:\\var\\lib\\tomcat7\\sysauth\\policy\\docTypeFile";
				//String desFile="D:\\var\\lib\\tomcat7\\sysauth\\policy.zip";
				try {
					//zipFile(srcFile, desFile,"123456");
					
				/*String srcZipFile="D:\\var\\lib\\tomcat7\\sysauth\\policy.zip";
				zip(srcZipFile,"D:\\var\\lib\\tomcat7\\sysauth\\policy");
				 
				//String srcZipFile="doc/zip_test/work.zip";
				String desZipFile="D:\\var\\lib\\tomcat7\\sysauth\\policy_Encrypter.zip";
				encryptZipFile(srcZipFile, desZipFile,"123456");*/
					String desZipFile="D:\\var\\lib\\tomcat7\\sysauth\\policy_Encrypter.zip";
				 
				//decrypterZipFile(desZipFile,"alarmFile", "D:\\var\\lib\\tomcat7\\sysauth\\restore\\alarmFile","123456");
				/*String decrypterSrcZipFile="doc/zip_test/work_Encrypter.zip";
				String extractFileName="1.xlsx";
				String decrypterDesFile="doc/zip_test/1_decrypter.xlsx";
				decrypterZipFile(decrypterSrcZipFile,extractFileName, decrypterDesFile,"123456");*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

}
