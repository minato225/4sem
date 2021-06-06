#include<stdio.h>
#include<stdlib.h>
#include <string.h>

#pragma warning(disable:4996)

#define MAX_LENGTH 100
typedef struct employee {
	int id;
	char name[10];
	float hours;
}employee;

int main(int argc, char* args[])
{
	char* bFileName = args[1];
	int countRec = atoi(args[2]);

	FILE* file = fopen(bFileName, "wb");
	if (file == 0) {
		puts("ERROR");
		return 0;
	}
	employee data[MAX_LENGTH];
	int i;
	for (i = 0; i < countRec; i++)
	{
		printf("%d Employee\n", i + 1);
		printf("Name: ");
		if (scanf("%s", &data[i].name)==0)puts("ERROR in scan");

		printf("Hours: ");
		if (scanf("%f", &data[i].hours)==0)puts("ERROR in scan");

		data[i].id = i + 1;
		printf("\n");
	}

	fwrite(&data, sizeof(employee), countRec, file);
	fclose(file);
}