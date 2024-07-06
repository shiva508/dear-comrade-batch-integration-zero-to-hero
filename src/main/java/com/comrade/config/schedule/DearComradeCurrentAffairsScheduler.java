package com.comrade.config.schedule;

import com.comrade.entity.TopicDetailsEntity;
import com.comrade.entity.TopicEntity;
import com.comrade.repository.TopicDetailsRepository;
import com.comrade.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DearComradeCurrentAffairsScheduler {
    private final TopicRepository topicRepository;
    private final TopicDetailsRepository topicDetailsRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;
    //boolean hasRecords = true;


    //@Scheduled(fixedRate = 60_00)
    public void fetchLatestCurrentAffairs(){
        log.info("Running fetchLatestCurrentAffairs");
        List<TopicEntity> topics = topicRepository.findAll();
        topics.parallelStream().forEach(topicEntity -> {
            topicEntity.setShortDescription("INDIA");
            TopicDetailsEntity topicDetailsEntity = new TopicDetailsEntity();
            topicDetailsEntity.setTopicId(topicEntity.getId());
            topicDetailsEntity.setDescription(" We all proud of INDIA");
            topicDetailsRepository.save(topicDetailsEntity); //Tx begins and commits
          /* What happens if crash happens here
           */
            topicRepository.save(topicEntity); //Tx begins and commits
        });
    }

    /*
    This approach offers recoverability
    what if it has high data
     */
    @Transactional
    //@Scheduled(fixedRate = 60_00)
    public void fetchLatestCurrentAffairsTx(){
        log.info("Running fetchLatestCurrentAffairs");
        List<TopicEntity> topics = topicRepository.findTop10ByStatusOrderByCreatedDateAsc("IN_PROGRESS");
        topics.parallelStream().forEach(topicEntity -> {
            topicEntity.setShortDescription("INDIA");
            TopicDetailsEntity topicDetailsEntity = new TopicDetailsEntity();
            topicDetailsEntity.setTopicId(topicEntity.getId());
            topicDetailsEntity.setDescription(" We all proud of INDIA");
            topicDetailsRepository.save(topicDetailsEntity); //Tx begins and commits
            /* What happens if crash happens here
             */
            topicEntity.setStatus("COMPLETE");
            topicRepository.save(topicEntity); //Tx begins and commits
        });
    }
/*
Above takes too long to complete tasks, but my ask is to complete all eligible records
But it creates  reference topics each time still it has high memory
This is long run transaction @TX on method level
We Need short run transactions
 */
    @Transactional
    //@Scheduled(fixedRate = 60_00)
    public void fetchLatestCurrentAffairsTxWhile(){
        boolean hasRecords = true;
        log.info("hasRecords ? {}",hasRecords);
        while (hasRecords){
            log.info("Running fetchLatestCurrentAffairs");
            List<TopicEntity> topics = topicRepository.findTop10ByStatusOrderByCreatedDateAsc("IN_PROGRESS");
            hasRecords = !topics.isEmpty();
            if (!hasRecords){
                break;
            }
            topics.parallelStream().forEach(topicEntity -> {
                topicEntity.setShortDescription("INDIA");
                TopicDetailsEntity topicDetailsEntity = new TopicDetailsEntity();
                topicDetailsEntity.setTopicId(topicEntity.getId());
                topicDetailsEntity.setDescription(" We all proud of INDIA");
                topicDetailsRepository.save(topicDetailsEntity); //Tx begins and commits
                /* What happens if crash happens here
                 */
                topicEntity.setStatus("COMPLETE");
                topicRepository.save(topicEntity); //Tx begins and commits
            });
        }

    }


    @Scheduled(fixedRate = 60_000)
    public void fetchLatestCurrentAffairsCustomTx(){
        boolean hasRecords = true;
        log.info("hasRecords ? {}",hasRecords);
        while (hasRecords){

            hasRecords = Boolean.TRUE.equals(transactionTemplate.execute(status -> {
                log.info("Running fetchLatestCurrentAffairs");
                List<TopicEntity> topics = topicRepository.findTop10ByStatusOrderByCreatedDateAsc("IN_PROGRESS");
                log.info("Size {}", topics.size());
                if (topics.isEmpty()) {
                    log.info("Empty Size");
                    return false;
                }
                topics.parallelStream().forEach(topicEntity -> {
                    topicEntity.setShortDescription("INDIA");
                    TopicDetailsEntity topicDetailsEntity = new TopicDetailsEntity();
                    topicDetailsEntity.setTopicId(topicEntity.getId());
                    topicDetailsEntity.setDescription("We all proud of INDIA");
                    topicDetailsRepository.save(topicDetailsEntity);
                    topicEntity.setStatus("COMPLETE");
                    topicRepository.save(topicEntity);
                });
                log.info("===========");
                return true;
            }));
            //log.info("Final {}", hasRecords);
        }



    }
}
