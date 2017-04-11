#ifndef UNION_SEARCH_H_INCLUDED
#define UNION_SEARCH_H_INCLUDED
#define maxlength 1000
#include <iostream>
using namespace std;
template<class T>
class uni
{
private:
    struct node
    {
        T data;
        int parent;
    };
    node a[maxlength];
    int length;
public:
    void create();
    void trans();
    void display();
    void watch();
};
template<class T>
void uni<T>::watch()
{
    for(int i=1; i<=length; i++)
    {
        cout<<a[i].data<<" ";
    }
    cout<<endl;
    for(int i=1; i<=length; i++)
    {
        cout<<a[i].parent<<" ";
    }
}
template<class T>
void uni<T>::display()
{
    for(int i=1; i<=length; i++)
    {
        if(a[i].parent==i)
        {
            cout<<"{"<<a[i].data;
            for(int j=1; j<=length; j++)
            {
                if(a[j].parent==a[i].parent&&i!=j) cout<<","<<a[j].data;
                if(j==length) cout<<"}"<<endl;
            }
        }
    }
}
template<class T>
void uni<T>::trans()
{
    int i,k;
    T x,y;
    cout<<"input the pairs of the equal elements:"<<endl<<">>";
    cin>>k;
    for(int j=1; j<=k; j++)
    {
        // watch();
        // cout<<endl;
        cout<<"input the #"<<j<<"pairs of the equal elements"<<endl<<">>";
        cin>>x>>y;
        i=1;
        while(i<=length)
        {
            if(a[i].data==x) break;
            i++;
        }
        for(int m=1; m<=length; m++)
        {
            if(m!=i&&a[m].data==y)
            {
                if(m>1) a[m].parent=a[i].parent;
                else a[i].parent=a[m].parent;
            }
        }
    }

}
template<class T>
void uni<T>::create()
{
    do
    {
        cout<<"input the number of the elements:"<<endl<<">>";
        cin>>length;
    }
    while(length>maxlength-1);
    cout<<"input all the elements:"<<endl<<">>";
    for(int i=1; i<=length; i++)
    {
        cin>>a[i].data;
        a[i].parent=i;
    }
}



#endif // UNION_SEARCH_H_INCLUDED
