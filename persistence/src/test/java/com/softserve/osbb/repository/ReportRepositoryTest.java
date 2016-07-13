package com.softserve.osbb.repository;

import com.softserve.osbb.PersistenceConfiguration;
import com.softserve.osbb.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by nazar.dovhyy on 04.07.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceConfiguration.class)
@Transactional
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    private Report report;

    @Before
    public void init() {
        report = new Report();
        LocalDate dateCreation = LocalDate.now();
        report = new Report();
        report.setName("баланс ЧЕРВ/2016");
        report.setCreationDate(dateCreation);
    }


    @Test
    public void testSaveReport() {
        report = reportRepository.save(report);
        assertNotNull(report);
        assertTrue(reportRepository.exists(report.getReportId()));
    }

    @Test
    public void testUpdateReport() {
        report = reportRepository.save(report);
        report.setName("баланс ЧЕРВ/2016 2.0");
        Report updatedReport;
        updatedReport = reportRepository.saveAndFlush(report);
        assertSame(report.getReportId(), updatedReport.getReportId());
    }


    @Test
    public void testDeleteReport() {
        report = reportRepository.save(report);
        reportRepository.delete(report);
        assertFalse(reportRepository.exists(report.getReportId()));
    }

    @Test
    public void testGetAllReports() {
        List<Report> reports = new ArrayList<>();
        reports.add(new Report("баланс ЛИП/2016", "фін. звіт за липень"));
        reports.add(new Report("баланс СЕР/2016", "фін. звіт за серпень"));
        reports.add(new Report("баланс ВЕР/2016", "фін. звіт за вересень"));
        List<Report> savedReports = reportRepository.save(reports);
        assertTrue(savedReports.size()==reports.size());
    }

    @Test
    public void testDeleteAllReports() {
        reportRepository.deleteAll();
        assertTrue(reportRepository.findAll().isEmpty());

    }

    @Test
    public void testGetAllReportsByYear() {
        List<Report> reportList = new ArrayList<>();
        Report reportOne = new Report();
        reportOne.setName("report1");
        reportOne.setCreationDate(LocalDate.now().minusYears(2));
        reportList.add(reportOne);
        Report reportTwo = new Report();
        reportTwo.setName("report2");
        reportTwo.setCreationDate(LocalDate.now().minusYears(1));
        reportList.add(reportTwo);
        Report reportThree = new Report();
        reportThree.setName("report3");
        reportThree.setCreationDate(LocalDate.now().minusYears(2));
        reportList.add(reportThree);

        reportRepository.save(reportList);

        List<Report> reportsByYear = reportRepository.findByCreationDate(LocalDate.now().minusYears(2));
        assertTrue(reportsByYear.size() == 2);
    }


}
