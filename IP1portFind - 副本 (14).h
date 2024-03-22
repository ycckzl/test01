// IP1portFind.h: interface for the CIP1portFind class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_IP1PORTFIND_H__4614E3B4_126B_43A7_BCB4_D5F94F05D31C__INCLUDED_)
#define AFX_IP1PORTFIND_H__4614E3B4_126B_43A7_BCB4_D5F94F05D31C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CIP1portFind  
{		
	struct FindNumNode
	{
		DWORD	srcip;
		WORD	port;
		DWORD   value;
		FindNumNode	*next;
	public:
		FindNumNode(){next=NULL;}
	};
public:
	CIP1portFind();
	CIP1portFind(int len);
	virtual ~CIP1portFind();

	BOOL Add (WORD srcport, DWORD srcip, DWORD value);
	BOOL Del (WORD srcport, DWORD srcip);
	BOOL Find(WORD srcport, DWORD srcip, DWORD &value);	
	void Close();
	int  GetSize(){ return m_size;}
private:
	DWORD Hash(DWORD ip,WORD port);
	FindNumNode**m_HashTable;
	int     m_size;
	int     m_hashlen;          //数组长度
};

inline DWORD CIP1portFind::Hash(DWORD ip, WORD port)
{
	return DWORD (ip^port)%m_hashlen;
}

inline BOOL CIP1portFind::Find(WORD srcport, DWORD srcip, DWORD &value)
{
	DWORD item=Hash(srcip,srcport);
	FindNumNode	*pNode = m_HashTable[item];	
	while(pNode)
	{
		if (pNode->srcip==srcip&&pNode->port==srcport)
		{
			return TRUE;
		}		
		pNode = pNode->next;
	}
	return FALSE;
}
#endif // !defined(AFX_IP1PORTFIND_H__4614E3B4_126B_43A7_BCB4_D5F94F05D31C__INCLUDED_)
