package com.eric.treelist;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class TreeView extends ListActivity {
	private ArrayList<PDFOutlineElement> mPdfOutlinesCount = new ArrayList<>();
	private ArrayList<PDFOutlineElement> mPdfOutlinesCount_mid = new ArrayList<>();  //辅助
	private TreeViewAdapter treeViewAdapter = null;
	
	private Type type;
	private int exPandeLevel = 0;  //需要展开的层次
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setBackgroundResource(R.color.bg);
        initialData();
        treeViewAdapter = new TreeViewAdapter(this, R.layout.tree_item,
				mPdfOutlinesCount);
		setListAdapter(treeViewAdapter);
		registerForContextMenu(getListView());
		
    }
    
    private void initialData() {
    	type = new Type();
    	type.setId("dffgsd");
    	type.setTitle("顶级");
    	for(int i = 1;i <= 6; i ++){
    		Type child = new Type();
    		child.setId(i + "");
    		child.setTitle("类型" + i);
    		child.setParentType(type);
    		for(int j = 0;j < 3; j ++){
        		Type child2 = new Type();
        		child2.setId(i + "" + j);
        		child2.setTitle("类型" + i + j);
        		child2.setParentType(child);
        		child.getChildren_l().add(child2);
				for(int t = 0;t < 3; t ++){
					Type child3 = new Type();
					child3.setId(i + "" + j+""+t);
					child3.setTitle("类型" + i + j +t);
					child3.setParentType(child2);
					child2.getChildren_l().add(child3);
				}
        	}
    		type.getChildren_l().add(child);
    	}
    	initTypeToAdapter();
	}
    
    
    
    public void initTypeToAdapter(){
    	int level = 1;
		initChildren(type,level);
    }
    
    public void initChildren(Type type,int level){
    	if(type.getChildren_l().size() > 0){
			for(Type t : type.getChildren_l()){
				if(level <= exPandeLevel){
					makeExPande(t,level);
				}else if(level == (exPandeLevel + 1)){
					makeMidExPande(t,level);
				}else{
					makeUnExPande(t,level);
				}
				initChildren(t,level+1);
			}
		}
	}
    
    /*
     * 展开
     */
    public void makeExPande(Type t, int level){
    	mPdfOutlinesCount.add(makePDFOutlineElement(t,level,true));
		mPdfOutlinesCount_mid.add(makePDFOutlineElement(t,level,true));
    }
    
    /*
     * 中间层
     */
    public void makeMidExPande(Type t, int level){
    	mPdfOutlinesCount.add(makePDFOutlineElement(t,level,false));
		mPdfOutlinesCount_mid.add(makePDFOutlineElement(t,level,false));
    }
    
    /*
     * 不展开
     */
    public void makeUnExPande(Type t, int level){
		mPdfOutlinesCount_mid.add(makePDFOutlineElement(t,level,false));
    }
    
    
    public PDFOutlineElement makePDFOutlineElement(Type type,int level,boolean expanded){
		boolean mhasParent = false;
		String parentId;
		boolean mhasChild = false;
		if(type.getParentType() != null){
			mhasParent = true;
			parentId = type.getParentType().getId();
		}else{
			parentId = "";
		}
		if(type.getChildren_l().size() > 0){
			mhasChild = true;
		}
		PDFOutlineElement pdfOutlineElement = null;
		pdfOutlineElement = new PDFOutlineElement(type.getId(), type.getTitle(), mhasParent	, mhasChild, parentId, level,expanded);
    	return pdfOutlineElement;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private class TreeViewAdapter extends ArrayAdapter {
    	private int textViewResourceId;
		public TreeViewAdapter(Context context, int textViewResourceId,
				List<PDFOutlineElement> objects) {
			super(context, textViewResourceId, objects);
			mInflater = LayoutInflater.from(context);
			mfilelist = objects;
			mIconCollapse = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.outline_list_collapse);
			mIconExpand = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.outline_list_expand);
			this.textViewResourceId = textViewResourceId;
		}

		private LayoutInflater mInflater;
		private List<PDFOutlineElement> mfilelist;
		private Bitmap mIconCollapse;
		private Bitmap mIconExpand;


		public int getCount() {
			return mfilelist.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			convertView = mInflater.inflate(textViewResourceId, null);
			convertView.setTag(holder);
			return holder.makeView(position, convertView);
		}

		class ViewHolder {
			TextView text;
			ImageView icon;
			TextView txt_chose;
			@SuppressLint("ResourceAsColor")
			public View makeView(final int position, View convertView){
				this.text = (TextView) convertView.findViewById(R.id.text);
				this.icon = (ImageView) convertView.findViewById(R.id.icon);
				this.txt_chose = (TextView) convertView.findViewById(R.id.txt_chose);
				this.txt_chose.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "选择Id：" + mfilelist.get(position).getId(), Toast.LENGTH_SHORT).show();
					}
				});
				PDFOutlineElement pElement = mfilelist.get(position);
				if(pElement.isMhasChild()){
					this.txt_chose.setVisibility(View.GONE);
				}else{
					this.text.setTextColor(R.color.grey_dark);
				}
				int level = pElement.getLevel();
				this.icon.setPadding(40 * (level - 1), this.icon
						.getPaddingTop(), 0, this.icon.getPaddingBottom());
				this.text.setText(pElement.getOutlineTitle());
				if (pElement.isMhasChild()
						&& (pElement.isExpanded() == false)) {
					this.icon.setImageBitmap(mIconCollapse);
				} else if (pElement.isMhasChild()
						&& (pElement.isExpanded() == true)) {
					this.icon.setImageBitmap(mIconExpand);
				} else if (!pElement.isMhasChild()){
					this.icon.setImageBitmap(mIconCollapse);
					this.icon.setVisibility(View.INVISIBLE);
				}
				return convertView;
			}
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (!mPdfOutlinesCount.get(position).isMhasChild()) {
			Toast.makeText(this, mPdfOutlinesCount.get(position).getOutlineTitle(), Toast.LENGTH_LONG
			);
			return;
		}
		if (mPdfOutlinesCount.get(position).isExpanded()) {
			mPdfOutlinesCount.get(position).setExpanded(false);
			PDFOutlineElement pdfOutlineElement = mPdfOutlinesCount.get(position);
			ArrayList<PDFOutlineElement> temp=new ArrayList<PDFOutlineElement>();
			
			for (int i = position+1; i < mPdfOutlinesCount.size(); i++) {
				if (pdfOutlineElement.getLevel()>=mPdfOutlinesCount.get(i).getLevel()) {
					break;
				}
				temp.add(mPdfOutlinesCount.get(i));
			}
			
			mPdfOutlinesCount.removeAll(temp);
			
			treeViewAdapter.notifyDataSetChanged();
		} else {
			mPdfOutlinesCount.get(position).setExpanded(true);
			int level = mPdfOutlinesCount.get(position).getLevel();
			int nextLevel = level + 1;
			for (PDFOutlineElement pdfOutlineElement : mPdfOutlinesCount_mid) {
				int j=1;
				if (pdfOutlineElement.getParent()==mPdfOutlinesCount.get(position).getId()) {
					pdfOutlineElement.setLevel(nextLevel);
					pdfOutlineElement.setExpanded(false);
					mPdfOutlinesCount.add(position+j, pdfOutlineElement);
					j++;
				}			
			}
			treeViewAdapter.notifyDataSetChanged();
		}
	}

}