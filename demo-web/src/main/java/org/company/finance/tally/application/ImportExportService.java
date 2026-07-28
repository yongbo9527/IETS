package org.company.finance.tally.application;

import org.company.finance.tally.interfaces.rest.request.ImportExportRequestVO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImportExportService {

    void exportTemplate(HttpServletResponse response) throws IOException;

    void exportData(ImportExportRequestVO vo, HttpServletResponse response) throws IOException;

    String importData(MultipartFile file) throws Exception;
}
