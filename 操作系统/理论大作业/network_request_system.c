#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>

#define REQUEST_QUEUE_SIZE 100
#define TASK_QUEUE_SIZE 50
#define RESPONSE_QUEUE_SIZE 100
#define NUM_PRODUCERS 5
#define NUM_CONSUMERS 3
#define PRODUCER_LIFETIME 10 // 生产者线程运行时间（秒）

// 请求结构体
typedef struct {
    int id;
    char data[100];
} Request;

// 响应结构体
typedef struct {
    int id;
    char data[100];
} Response;

// 请求队列
Request request_queue[REQUEST_QUEUE_SIZE];
int request_count = 0;
int request_in = 0;
int request_out = 0;
bool request_produced = false; // 标记是否所有生产者线程都停止生成请求

// 任务队列
Request task_queues[NUM_CONSUMERS][TASK_QUEUE_SIZE];
int task_counts[NUM_CONSUMERS] = {0};
int task_ins[NUM_CONSUMERS] = {0};
int task_outs[NUM_CONSUMERS] = {0};

// 响应队列
Response response_queue[RESPONSE_QUEUE_SIZE];
int response_count = 0;
int response_in = 0;
int response_out = 0;

// 互斥锁和条件变量
pthread_mutex_t request_mutex;
pthread_cond_t request_not_empty;
pthread_cond_t request_not_full;

pthread_mutex_t task_mutexes[NUM_CONSUMERS];
pthread_cond_t task_not_empty[NUM_CONSUMERS];
pthread_cond_t task_not_full[NUM_CONSUMERS];

pthread_mutex_t response_mutex;
pthread_cond_t response_not_empty;
pthread_cond_t response_not_full;

// 生产者线程函数
void* producer(void* arg) {
    int producer_id = *((int*)arg);
    int request_id = 0;
    time_t start_time = time(NULL);

    while (true) {
        if (time(NULL) - start_time >= PRODUCER_LIFETIME) {
            printf("Producer %d stopped producing requests.\n", producer_id);
            break; // 生产者线程运行时间达到上限，停止生成请求
        }

        Request request;
        request.id = request_id++;
        snprintf(request.data, sizeof(request.data), "Request from producer %d", producer_id);

        // 加锁
        pthread_mutex_lock(&request_mutex);

        // 等待请求队列不满
        while (request_count == REQUEST_QUEUE_SIZE) {
            printf("Producer %d waiting for request queue not full.\n", producer_id);
            pthread_cond_wait(&request_not_full, &request_mutex);
        }

        // 将请求放入请求队列
        request_queue[request_in] = request;
        request_in = (request_in + 1) % REQUEST_QUEUE_SIZE;
        request_count++;

        printf("Producer %d produced request %d: %s\n", producer_id, request.id, request.data);

        // 通知请求处理线程请求队列不为空
        pthread_cond_signal(&request_not_empty);

        // 解锁
        pthread_mutex_unlock(&request_mutex);

        // 睡眠一定时间
        sleep(1);
    }

    // 标记生产者线程停止生成请求
    pthread_mutex_lock(&request_mutex);
    request_produced = true;
    pthread_mutex_unlock(&request_mutex);

    return NULL;
}

// 请求处理线程函数
void* request_handler(void* arg) {
    while (true) {
        Request request;

        // 加锁
        pthread_mutex_lock(&request_mutex);

        // 等待请求队列不为空，或者所有生产者线程都停止生成请求且请求队列为空
        while (request_count == 0 && !request_produced) {
            printf("Request handler waiting for request queue not empty.\n");
            pthread_cond_wait(&request_not_empty, &request_mutex);
        }

        if (request_count == 0 && request_produced) {
            // 所有生产者线程都停止生成请求且请求队列为空，退出请求处理线程
            printf("Request handler stopped handling requests.\n");
            pthread_mutex_unlock(&request_mutex);
            break;
        }

        // 从请求队列中取出请求
        request = request_queue[request_out];
        request_out = (request_out + 1) % REQUEST_QUEUE_SIZE;
        request_count--;

        printf("Request handler handled request %d: %s\n", request.id, request.data);

        // 通知生产者线程请求队列不满
        pthread_cond_signal(&request_not_full);

        // 解锁
        pthread_mutex_unlock(&request_mutex);

        // 根据请求类型将请求分配给相应的消费者线程
        int consumer_id = request.id % NUM_CONSUMERS;

        // 加锁
        pthread_mutex_lock(&task_mutexes[consumer_id]);

        // 等待任务队列不满
        while (task_counts[consumer_id] == TASK_QUEUE_SIZE) {
            printf("Request handler waiting for task queue %d not full.\n", consumer_id);
            pthread_cond_wait(&task_not_full[consumer_id], &task_mutexes[consumer_id]);
        }

        // 将请求放入任务队列
        task_queues[consumer_id][task_ins[consumer_id]] = request;
        task_ins[consumer_id] = (task_ins[consumer_id] + 1) % TASK_QUEUE_SIZE;
        task_counts[consumer_id]++;

        printf("Request handler assigned request %d to consumer %d.\n", request.id, consumer_id);

        // 通知消费者线程任务队列不为空
        pthread_cond_signal(&task_not_empty[consumer_id]);

        // 解锁
        pthread_mutex_unlock(&task_mutexes[consumer_id]);
    }
    return NULL;
}

// 消费者线程函数
void* consumer(void* arg) {
    int consumer_id = *((int*)arg);

    while (true) {
        Request request;

        // 加锁
        pthread_mutex_lock(&task_mutexes[consumer_id]);

        // 等待任务队列不为空
        while (task_counts[consumer_id] == 0) {
            printf("Consumer %d waiting for task queue not empty.\n", consumer_id);
            pthread_cond_wait(&task_not_empty[consumer_id], &task_mutexes[consumer_id]);
        }

        // 从任务队列中取出请求
        request = task_queues[consumer_id][task_outs[consumer_id]];
        task_outs[consumer_id] = (task_outs[consumer_id] + 1) % TASK_QUEUE_SIZE;
        task_counts[consumer_id]--;

        printf("Consumer %d consumed request %d: %s\n", consumer_id, request.id, request.data);

        // 通知请求处理线程任务队列不满
        pthread_cond_signal(&task_not_full[consumer_id]);

        // 解锁
        pthread_mutex_unlock(&task_mutexes[consumer_id]);

        // 处理请求并生成响应数据
        Response response;
        response.id = request.id;
        snprintf(response.data, sizeof(response.data), "Response for request %d from consumer %d", request.id, consumer_id);

        // 加锁
        pthread_mutex_lock(&response_mutex);

        // 等待响应队列不满
        while (response_count == RESPONSE_QUEUE_SIZE) {
            printf("Consumer %d waiting for response queue not full.\n", consumer_id);
            pthread_cond_wait(&response_not_full, &response_mutex);
        }

        // 将响应放入响应队列
        response_queue[response_in] = response;
        response_in = (response_in + 1) % RESPONSE_QUEUE_SIZE;
        response_count++;

        printf("Consumer %d produced response %d: %s\n", consumer_id, response.id, response.data);

        // 通知主线程响应队列不为空
        pthread_cond_signal(&response_not_empty);

        // 解锁
        pthread_mutex_unlock(&response_mutex);

        // 睡眠一定时间
        sleep(2);
    }
    return NULL;
}

// 主函数
int main() {
    pthread_t producer_threads[NUM_PRODUCERS];
    pthread_t request_handler_thread;
    pthread_t consumer_threads[NUM_CONSUMERS];
    int producer_ids[NUM_PRODUCERS];
    int consumer_ids[NUM_CONSUMERS];

    // 初始化互斥锁和条件变量
    pthread_mutex_init(&request_mutex, NULL);
    pthread_cond_init(&request_not_empty, NULL);
    pthread_cond_init(&request_not_full, NULL);

    for (int i = 0; i < NUM_CONSUMERS; i++) {
        pthread_mutex_init(&task_mutexes[i], NULL);
        pthread_cond_init(&task_not_empty[i], NULL);
        pthread_cond_init(&task_not_full[i], NULL);
    }

    pthread_mutex_init(&response_mutex, NULL);
    pthread_cond_init(&response_not_empty, NULL);
    pthread_cond_init(&response_not_full, NULL);

    // 创建生产者线程
    for (int i = 0; i < NUM_PRODUCERS; i++) {
        producer_ids[i] = i;
        pthread_create(&producer_threads[i], NULL, producer, &producer_ids[i]);
    }
    // 创建请求处理线程
    pthread_create(&request_handler_thread, NULL, request_handler, NULL);

    // 创建消费者线程
    for (int i = 0; i < NUM_CONSUMERS; i++) {
        consumer_ids[i] = i;
        pthread_create(&consumer_threads[i], NULL, consumer, &consumer_ids[i]);
    }

    // 等待生产者线程结束
    for (int i = 0; i < NUM_PRODUCERS; i++) {
        pthread_join(producer_threads[i], NULL);
    }

    // 等待请求处理线程结束
    pthread_join(request_handler_thread, NULL);

    // 等待消费者线程结束
    for (int i = 0; i < NUM_CONSUMERS; i++) {
        pthread_join(consumer_threads[i], NULL);
    }

    // 销毁互斥锁和条件变量
    pthread_mutex_destroy(&request_mutex);
    pthread_cond_destroy(&request_not_empty);
    pthread_cond_destroy(&request_not_full);

    for (int i = 0; i < NUM_CONSUMERS; i++) {
        pthread_mutex_destroy(&task_mutexes[i]);
        pthread_cond_destroy(&task_not_empty[i]);
        pthread_cond_destroy(&task_not_full[i]);
    }

    pthread_mutex_destroy(&response_mutex);
    pthread_cond_destroy(&response_not_empty);
    pthread_cond_destroy(&response_not_full);

    return 0;
}
