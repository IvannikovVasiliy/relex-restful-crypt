package ru.relex.service.impl;

import ru.relex.dto.DateIntervalDto;
import ru.relex.repository.dao.HistoryOperationDao;
import ru.relex.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final HistoryOperationDao historyOperationDao;

    public Map<String, Long> countOperations(DateIntervalDto intervalDto) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse(intervalDto.getDateFrom());
        Date end = dateFormat.parse(intervalDto.getDateTo());

        List<Timestamp> timestamps = historyOperationDao.findDates();
        long count = 0l;
        for (var ts : timestamps) {
            if (ts.after(start) && ts.before(end)) {
                count++;
            }
            if (ts.after(end)) {
                break;
            }
        }

        return Map.of("transaction_count", count);
    }
}
