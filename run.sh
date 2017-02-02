#!/bin/bash

set -e
set -o pipefail

function usage
{
    echo "usage: $0 [--JAR_NAME | -J <framework jar name>] [--YML_FILE | -Y <test yml file>] [--DB_URL | -D <jdbc end point>] [--USER_NAME | -U <user name>] [--USER_PASSWORD | -P <password>] [--LOGGER | -L <log4j properties file>] [--ACTIVE_CONN_COUNT | -C <active connections count> ] [--CONN_IDLE_TIMEOUT | -T <connection abandon timeout>] [--help | -h]]"
}

##### Main

while [ "$1" != "" ]; do
    case $1 in
	-J | --JAR_NAME )		shift
					jar_name=$1
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
	-C | --ACTIVE_CONN_COUNT )	shift
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

if [ -z "$jar_name" ] || [ -z "$yml_file" ] || [ -z "$db_url" ] || [ -z "$user_name" ] || [ -z "$password" ] ; then
    usage
    exit 1
fi

cmd="java -jar $jar_name --YML_FILE $yml_file --DB_URL $db_url --USER_NAME $user_name --USER_PASSWORD $password "
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

