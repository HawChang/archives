#ifndef openAddress_H_
#define openAddress_H_
#include <iostream>
using namespace std;

class hashTable
{
private:
    struct openAddressNode
{
    int data;
    bool used;
};
    int size;
    int count;
    openAddressNode *table;
public:
    hashTable();
    ~hashTable();
    void insert(int elem);
    void deletex(int elem);
    void print();
    int search(int elem);
};

hashTable::hashTable()
{
    cout<<"Please input size.\n";
    cin>>size;
    count=0;
    table=new openAddressNode[size];
    int i;
    for(i=0;i<size;i++)
    {
        table[i].data=0;
        table[i].used=0;
    }
}

hashTable::~hashTable()
{
    delete []table;
}

void hashTable::insert(int elem)
{
    if(count==size)
    {
        cout<<"hashTable is full! "<<elem<<" insert failed.\n";
        return;
    }
    int index=elem%size;
    while(table[index].used)
    {
        index++;
        index=index%size;
    }
    table[index].data=elem;
    table[index].used=1;
    count++;
}

void hashTable::print()
{
    int i=0;
    for(i=0;i<size;i++)
    {
        if(table[i].used)
            cout<<i<<"  "<<table[i].data<<endl;
    }
}

void hashTable::deletex(int elem)
{
    int index=search(elem);
    if(index!=-1)
    {
        table[index].data=0;
        table[index].used=0;
        count--;
    }
}

int hashTable::search(int elem)
{
    int index=elem%size;
    int flag=index;
    while(table[index].data!=elem)
    {
        index++;
        index=index%size;
        if(!table[index].used || index==flag)
        {
            return -1;
        }
    }
    return index;
}

#endif
