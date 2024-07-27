package br.com.alunoonline.api.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduleTask {

    @Scheduled(fixedRate = 30000)
    public void importaAluno(){
        log.info("Importa aluno");
    }
}
