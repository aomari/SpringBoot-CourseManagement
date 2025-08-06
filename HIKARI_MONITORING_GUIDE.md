# HikariCP Monitoring and Troubleshooting Guide

## What is the Thread Starvation Warning?

The warning you saw indicates that HikariCP's housekeeper thread was delayed significantly (over 10 minutes). This can happen due to:

1. **System overload** - High CPU usage or memory pressure
2. **Long garbage collection pauses** - JVM GC blocking threads
3. **Database performance issues** - Slow queries or connection problems
4. **Clock synchronization issues** - System time jumps

## Configuration Added

We've added optimized HikariCP settings to your `application.properties`:

```properties
# HikariCP Connection Pool Configuration
spring.datasource.hikari.maximum-pool-size=10        # Max connections
spring.datasource.hikari.minimum-idle=5              # Min idle connections
spring.datasource.hikari.idle-timeout=300000         # 5 minutes
spring.datasource.hikari.max-lifetime=1200000        # 20 minutes
spring.datasource.hikari.connection-timeout=20000    # 20 seconds
spring.datasource.hikari.validation-timeout=3000     # 3 seconds
spring.datasource.hikari.leak-detection-threshold=60000  # 60 seconds
```

## Monitoring Endpoints

After adding Spring Boot Actuator, you can monitor your application at:

- **Health**: `http://localhost:8080/actuator/health`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **HikariCP Metrics**: `http://localhost:8080/actuator/metrics/hikaricp.connections`

### Key HikariCP Metrics to Monitor:

1. `hikaricp.connections.active` - Currently active connections
2. `hikaricp.connections.idle` - Currently idle connections
3. `hikaricp.connections.pending` - Threads waiting for connections
4. `hikaricp.connections.timeout` - Connection timeouts
5. `hikaricp.connections.usage` - Connection usage time

## Troubleshooting Steps

### 1. Check System Resources
```bash
# Check CPU and memory usage
top -p $(pgrep java)

# Check disk I/O
iostat -x 1 5
```

### 2. Monitor Database Performance
```sql
-- Check for long-running queries (PostgreSQL)
SELECT pid, now() - pg_stat_activity.query_start AS duration, query 
FROM pg_stat_activity 
WHERE (now() - pg_stat_activity.query_start) > interval '5 minutes';

-- Check connection count
SELECT count(*) FROM pg_stat_activity;
```

### 3. JVM Monitoring
```bash
# Check GC activity
jstat -gc [PID] 5s

# Generate thread dump if needed
jstack [PID] > threaddump.txt
```

### 4. Application Logs
Look for patterns in your application logs:
- Frequent connection timeouts
- Database query errors
- Memory warnings
- GC logs (if enabled)

## Prevention Strategies

1. **Optimize Database Queries**
   - Use proper indexes
   - Avoid N+1 query problems
   - Use connection pooling appropriately

2. **JVM Tuning**
   - Use G1 garbage collector for better latency
   - Set appropriate heap sizes
   - Monitor GC pause times

3. **Connection Pool Sizing**
   - Formula: `pool_size = (core_count * 2) + effective_spindle_count`
   - For most applications: 5-10 connections is sufficient
   - Monitor actual usage before increasing

4. **Regular Monitoring**
   - Set up alerts for connection pool metrics
   - Monitor database performance
   - Track application response times

## Emergency Response

If you see this warning frequently:

1. **Immediate**: Check system resources and database connectivity
2. **Short-term**: Restart the application if resources are constrained
3. **Long-term**: Analyze logs, optimize queries, and tune JVM parameters

## Useful Commands

```bash
# Restart application with better JVM settings
java -jar your-app.jar \
  -Xms512m -Xmx1024m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+PrintGCDetails \
  -XX:+PrintGCTimeStamps

# Check HikariCP metrics via curl
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active

# Monitor connection pool in real-time
watch -n 2 'curl -s http://localhost:8080/actuator/metrics/hikaricp.connections | jq'
```