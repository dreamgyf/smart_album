package com.newbee.smart_album.service;

import com.newbee.smart_album.entity.Count;

import java.io.File;

public interface AsyncTaskService {

    void photoUploadTask(int userId, int albumId, String prefix, String suffix, String uploadPath, File uploadFile, Count count);
}
