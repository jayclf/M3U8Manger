package com.cuilifan.m3u8;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Network {
    public void download(String url, DownloadListener downloadListener) {
        new DownloadTask(downloadListener).execute(url);
    }

    private final class DownloadTask extends AsyncTask<String, Integer, File> {

        private DownloadListener downloadListener;
        private String mError;

        public DownloadTask(DownloadListener downloadListener) {
            this.downloadListener = downloadListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadListener.onStart();
        }

        @Override
        protected File doInBackground(String... strings) {
            try {
                // 进行下载
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                String contentType = httpURLConnection.getContentType();
                int contentLength = httpURLConnection.getContentLength();
                if (TextUtils.equals("audio/x-mpegurl", contentType)) {
                    mError = "invalid content type:".concat(contentType);
                    return null;
                }
                if (200 != httpURLConnection.getResponseCode()) {
                    mError = "invalid response code:".concat(String.valueOf(httpURLConnection.getResponseCode()));
                    return null;
                }
                String[] split = url.getFile().split("/");
                String fileName = split[split.length - 1];
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int totalRead = 0;
                int len = 0;
                while (0 < (len = bufferedInputStream.read(buffer))) {
                    fileOutputStream.write(buffer, 0, len);
                    totalRead += len;
                    publishProgress(totalRead, contentLength);
                }
                bufferedInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                mError = "exception occur:".concat(e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            downloadListener.onProgress(values[0], values[1]);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (null == file)  {
                downloadListener.onFailed(mError);
            } else {
                downloadListener.onFinish(file);
            }
        }
    }

    public interface DownloadListener {
        void onStart();
        void onProgress(int currentLength, int totalLength);
        void onFinish(File file);
        void onFailed(String error);
    }
}
