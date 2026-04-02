package org.company.finance.application.service;

import org.company.finance.application.vo.request.ImportExportRequestVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImportExportService {

    void exportTemplate(HttpServletResponse response) throws IOException;

    void exportData(ImportExportRequestVO vo, HttpServletResponse response) throws IOException;

    String importData(MultipartFile file) throws Exception;
}
