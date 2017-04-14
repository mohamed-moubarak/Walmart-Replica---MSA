# Walmart-Replica---MSA
This is a repo for a Walmart Store replica, developed for the course "Architecture of Massively Scalable Apps, CSEN1073" taught in the faculty of Media Engineering and Technology, Computer Science and Engineering Major, in the German University in Cairo

The following are the instructions to follow to setup the load balancer of this project

# Install haproxy

sudo apt-get install haproxy

# Edit the config file of haproxy

sudo nano /etc/haproxy/haproxy.cfg

replace the contents of the file with the following

-----------------------------------------------------

global
log /dev/log	local0
log /dev/log	local1 notice
chroot /var/lib/haproxy
user haproxy
group haproxy
daemon

defaults
log	global
mode	http
option	httplog
option	dontlognull
retries 3
option redispatch
contimeout 5000
clitimeout 50000
srvtimeout 50000

listen rabbitmq 0.0.0.0:5000
mode http
stats enable
stats uri /haproxy?stats
stats realm Strictly\ Private
stats auth admin:admin 
log global 
option httpclose
option forwardfor
balance leastconn
server rabbitmq1 localhost:8083 check
server rabbitmq2 localhost:8084 check
server rabbitmq3 localhost:8085 check

-----------------------------------------------------
