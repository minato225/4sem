#include<stdio.h>
#include<stdlib.h>
#include <string.h>

#pragma warning(disable:4996)

typedef struct employee {
	int id;
	char name[10];
	float hours;
} employee;

int main(int argc, char* argv[]) {
	char* bFileName = argv[1];
	char* oFileName = argv[2];
	long float salary = atoi(argv[3]);

	FILE* bFile = fopen(bFileName,"rb");
	if (bFile == 0) {
		puts("ERROR");
		return 0;
	}
	FILE* oFile = fopen(oFileName,"a");
	if (oFile == 0) {
		puts("ERROR");
		return 0;
	}

	fprintf(oFile, "Report on file \"%s\"\n", bFileName);
	fprintf(oFile, "id - name - hours - salary\n");

	employee data;
	while (fread(&data, sizeof(employee), 1, bFile) == 1)
		fprintf(oFile, "%d - %s -- %f - %f\n", data.id, data.name, data.hours, data.hours * salary);	

	fclose(bFile);
	fclose(oFile);
	return 0;
}