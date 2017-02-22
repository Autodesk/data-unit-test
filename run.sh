#!/bin/bash

set -e
set -o pipefail

function usage
{
    echo "usage: $0 [--MAIN_JAR_LOCATION | -M <main jar location containing the application jar>] [--EXTRA_CLASSPATH | -E <location of extra jars containing the external driver class>] [--YML_FILE | -Y <test yml file>] [--DB_URL | -D <jdbc end point>] [--USER_NAME | -U <user name>] [--USER_PASSWORD | -P <password>] [--JDBC_DRIVER | -J <JDBC driver class name>] [--LOGGER | -L <log4j properties file>] [--ACTIVE_CONN_COUNT | -A <active connections count> ] [--CONN_IDLE_TIMEOUT | -T <connection abandon timeout>] [--help | -h]]"
}

##### Main

while [ "$1" != "" ]; do
    case $1 in
	-M | --MAIN_JAR_LOCATION )	shift
					main_jar=$1
					;;
	-E | --EXTRA_CLASSPATH )	shift
					extra_classpath=$1
					;;
        -Y | --YML_FILE )		shift
                                	yml_file=$1
                                	;;
	-D | --DB_URL )			shift
					db_url=$1
					;;
	-U | --USER_NAME )		shift
					user_name=$1
					;;
	-P | --USER_PASSWORD )		shift
					password=$1
					;;
	-L | --LOGGER )			shift
					logger=$1
					;;
	-J | --JDBC_DRIVER )		shift
					jdbc_driver=$1
					;;
	-A | --ACTIVE_CONN_COUNT )	shift
					conn_count=$1
					;;
	-T | --CONN_IDLE_TIMEOUT )	shift
					timeout=$1
					;;
        -h | --help )           	usage
                                	exit
                                	;;
        * )                     	usage
                                	exit 1
    esac
    shift
done

if [ -z "$main_jar" ] || [ -z "$yml_file" ] || [ -z "$db_url" ] || [ -z "$user_name" ] || [ -z "$password" ] ; then
    usage
    exit 1
fi

if [ ! -z "$jdbc_driver" ] && [ -z "$extra_classpath" ] ; then
    echo "If external driver is provided, you need to provide the jar name containing the driver as well"
    usage
    exit 1
fi

cmd="java -cp $main_jar"

if [ ! -z "$extra_classpath" ] ; then
    cmd="$cmd:$extra_classpath"
fi

cmd="$cmd com.autodesk.adp.validation_framework.TestRunner --YML_FILE $yml_file --DB_URL $db_url --USER_NAME $user_name --USER_PASSWORD $password"

if [ ! -z "$jdbc_driver" ] ; then
    cmd="$cmd --JDBC_DRIVER $jdbc_driver"
fi

if [ ! -z "$logger" ]; then
    cmd="$cmd --LOGGER $logger"
fi

if [ ! -z "$conn_count" ]; then
    cmd="$cmd --ACTIVE_CONN_COUNT $conn_count"
fi

if [ ! -z "$timeout" ]; then
    cmd="$cmd --CONN_IDLE_TIMEOUT $timeout"
fi

echo "executing command: $cmd"
exec $cmd

