#include<Windows.h>
#include<stdio.h>
#include<stdlib.h>
#include<conio.h>
#include <wchar.h>

#pragma warning(disable:4996)
#pragma warning(disable:6031)
#pragma warning(disable:6054)

typedef struct employee {
	int id;
	char name[10];
	float hours;
} employee;

int main() {
	TCHAR createPath[100] = L"Creator.exe ";

	TCHAR pathBFile[10];
	printf("Enter name of binary:\n");
	_getws(pathBFile);

	TCHAR countRec[10];
	printf("Enter count of records:\n");
	_getws(countRec);

	wcscat(createPath, pathBFile);
	wcscat(createPath, L" ");
	wcscat(createPath, countRec);

	STARTUPINFO siCreator = { sizeof(STARTUPINFO) };
	PROCESS_INFORMATION piCreator = {NULL};

	if (!CreateProcess(NULL, createPath, NULL, NULL, FALSE, 0, NULL, NULL, &siCreator, &piCreator))
	{
		printf("The mew process is not created.\n Check a name of the process.\n");
		return 0;
	}

	WaitForSingleObject(piCreator.hProcess, INFINITE);

	FILE* bFile = _wfopen(pathBFile, L"rb");
	if (bFile == 0) {
		puts("ERROR");
		return 0;
	}

	employee data;
	while (fread(&data, sizeof(employee), 1, bFile) == 1)
		printf("%d - %s - %f\n", data.id, data.name, data.hours);

	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	TCHAR reporterPath[80] = L"Reporter.exe ";

	TCHAR pathOFile[10];
	printf("Enter name of report file \n");
	_getws(pathOFile);

	TCHAR salary[10];
	printf("Enter salary per hour\n");
	_getws(salary);


	wcscat(reporterPath, pathBFile);
	wcscat(reporterPath,L" ");
	wcscat(reporterPath, pathOFile);
	wcscat(reporterPath,L" ");
	wcscat(reporterPath, salary);

	STARTUPINFO siReporter = { sizeof(STARTUPINFO) };
	PROCESS_INFORMATION piReporter = { NULL };

	if (!CreateProcess(NULL, reporterPath, NULL, NULL, FALSE, 0, NULL, NULL, &siReporter, &piReporter))
	{
		printf("The mew process is not created.\n Check a name of the process.\n");
		return 0;
	}
	WaitForSingleObject(piReporter.hProcess, INFINITE);

	FILE* oFile = _wfopen(pathOFile, L"r");
	if (oFile == 0) {
		puts("ERROR");
		return 0;
	}

	char str[80];
	fgets(str, 80, oFile);
	while (!feof(oFile))
	{
		puts(str);
		fgets(str, 80, oFile);
	}

	getch();
	fclose(oFile);
	fclose(bFile);

	CloseHandle(piReporter.hThread);
	CloseHandle(piCreator.hThread);

	CloseHandle(piReporter.hProcess);
	CloseHandle(piCreator.hProcess);

	return 0;
}