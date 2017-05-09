# Walmart-Replica---MSA
This is a repo for a Walmart Store replica, developed for the course "Architecture of Massively Scalable Apps, CSEN1073" taught in the faculty of Media Engineering and Technology, Computer Science and Engineering Major, in the German University in Cairo


# HAPROXY LOADBALANCER

1- sudo cd /etc/haproxy/
2- replace content of haproxy.cfg with the following
3- sudo servvice haproxy restart

global
        log 127.0.0.1   local1
        maxconn 4096
        #chroot /usr/share/haproxy
        user haproxy
        group haproxy
        daemon
        #debug
        #quiet
 
defaults
        log     global
        mode    tcp
        option  tcplog
        retries 3
        option redispatch
        maxconn 2000
        timeout connect 5000
        timeout client 50000
        timeout server 50000
 
listen  stats :1936
        mode http
        stats enable
        stats hide-version
        stats realm Haproxy\ Statistics
        stats uri /
 
listen aqmp_front :5673
        mode            tcp
        balance         roundrobin
        timeout client  3h
        timeout server  3h
        option          clitcpka
        server          aqmp-1 localhost:5672  check inter 5s rise 2 fall 3