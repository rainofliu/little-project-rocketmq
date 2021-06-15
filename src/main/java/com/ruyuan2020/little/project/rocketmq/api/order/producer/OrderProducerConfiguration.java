package com.ruyuan2020.little.project.rocketmq.api.order.producer;

import com.ruyuan2020.little.project.rocketmq.api.order.listener.FinishedOrderTransactionListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 订单服务消息生产者配置
 *
 * @author ajin
 */
@Configuration
public class OrderProducerConfiguration {

    @Value("${rocketmq.namesrv.address}")
    private String namesrvAddress;

    @Value("${rocketmq.order.producer.group}")
    private String orderProducerGroup;

    @Value("${rocketmq.order.finished.producer.group}")
    private String orderPayProducerGroup;

    @Bean(value = "orderMqProducer")
    public DefaultMQProducer orderMqProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(orderProducerGroup);
        producer.setNamesrvAddr(namesrvAddress);
        producer.start();
        return producer;
    }

    /**
     * 订单事务消息生产者
     *
     * @return 订单事务消息rocketmq的生产者对象
     */
    @Bean(value = "orderFinishedTransactionMqProducer")
    public TransactionMQProducer orderTransactionMqProducer(@Qualifier(value = "finishedOrderTransactionListener")
        FinishedOrderTransactionListener finishedOrderTransactionListener) throws MQClientException {

        TransactionMQProducer producer = new TransactionMQProducer(orderPayProducerGroup);
        producer.setNamesrvAddr(namesrvAddress);

        // 事务回调线程池处理
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(2000), r -> {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            });

        producer.setExecutorService(executorService);
        producer.setTransactionListener(finishedOrderTransactionListener);

        producer.start();
        return producer;
    }
}
