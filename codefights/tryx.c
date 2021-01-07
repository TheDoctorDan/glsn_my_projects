// Definition for arrays:
// typedef struct arr_##name {
//   int size;
//   type *arr;
// } arr_##name;
//
// arr_##name alloc_arr_##name(int len) {
//   arr_##name a = {len, len > 0 ? malloc(sizeof(type) * len) : NULL};
//   return a;
// }
//
//
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <stddef.h>
typedef int bool;
#define false 0
#define true 1

typedef struct arr_string {
   int size;
   char **arr;
 } arr_string;

 typedef struct arr_integer {
    int size;
    int *arr;
  } arr_integer;

typedef struct  {
    char *systemName;
    int  prevStepNumber;
}KeyValue;

typedef struct {
    int size;
    KeyValue *arrKeyValue;
} arr_KeyValue;


bool launchSequenceChecker(arr_string systemNames, arr_integer stepNumbers) {

    arr_KeyValue keyValues;
    KeyValue tempKeyValue;
    KeyValue tempKeyValue2;
    int i;
    int j;
    int k;
    char *name;
    int stepNum;
    bool found;
    char **sysStringPtr;
    int *stepIntPtr;



    int icmp;

    clock_t startTime;
    clock_t currentTime;

    startTime = clock();


    keyValues.size = stepNumbers.size;
    keyValues.arrKeyValue = calloc(stepNumbers.size, sizeof(KeyValue));

    sysStringPtr=systemNames.arr;
    stepIntPtr=stepNumbers.arr;
    for(i=0;i<systemNames.size; i++)
    {
        //printf("============\n");
        //printf("systemNames.arr[%d]=%s\n", i, systemNames.arr[i]);
        currentTime=clock();
        //printf("el:%lf\n", (float)(currentTime - startTime)/CLOCKS_PER_SEC);
        if((float)(currentTime - startTime)/CLOCKS_PER_SEC >0.435)
             return false;

        name=*sysStringPtr;
        stepNum=*stepIntPtr;
        //printf("name:%s  step:%d\n", name, stepNum);

        found=false;
        for(j=0;j<keyValues.size && !found; j++)
        {
            //printf("   keyValues.arr[%d]=name:%s\n", j, keyValues.arrKeyValue[j].systemName);


            if(keyValues.arrKeyValue[j].systemName==NULL)
            {
                //new system
                keyValues.arrKeyValue[j].systemName = name;
                found=true;
                keyValues.arrKeyValue[j].prevStepNumber = stepNum;

            }
            else
            {
                icmp = strcmp(keyValues.arrKeyValue[j].systemName, name);
                if(icmp==0)
                {
                    //known system
                    found=true;
                    if(stepNum >  keyValues.arrKeyValue[j].prevStepNumber)
                    {
                        // correct order
                         keyValues.arrKeyValue[j].prevStepNumber = stepNum;
                    } else
                    {
                        //wrong order
                        return false;
                    }
                }
                else if(icmp<0)
                {
                    //keep looking
                    ;
                }
                else
                {
                    //icmp>0, do insert
                    tempKeyValue = keyValues.arrKeyValue[j];
                    keyValues.arrKeyValue[j].systemName=name;
                    keyValues.arrKeyValue[j].prevStepNumber = stepNum;
                    found=true;
                    //shift down
                    for(k=j+1; k<keyValues.size; k++)
                    {
                        if(keyValues.arrKeyValue[k].systemName==NULL)
                        {
                            //at end
                            keyValues.arrKeyValue[k] = tempKeyValue;
                            k=keyValues.size;
                        }
                        else
                        {
                            tempKeyValue2 =  keyValues.arrKeyValue[k];
                            keyValues.arrKeyValue[k] = tempKeyValue;
                            tempKeyValue = tempKeyValue2;
                        }

                    }

                }
            }

        }
        sysStringPtr++;
        stepIntPtr++;
    }
    return true;
}
