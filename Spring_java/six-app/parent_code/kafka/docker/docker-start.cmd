for /f "delims=[] tokens=2" %%a in ('ping -4 -n 1 %ComputerName% ^| findstr []') do set NetworkIP=%%a
echo NetworkIP: %NetworkIP%
set DOCKERHOST=%NetworkIP%
docker-compose -f docker-compose.yaml up