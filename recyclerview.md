requestLayout
		detachAllView、添加到 mAttachScrap集合中
		遍历集合：getViewForPosition：从mAttachScrap中移除一个，并且attachToParentView上
		从mAttachScrap中取出剩余未使用项，从mAttachScrap中移除，并调用removeDetachView方法

notifyDataSetChange
		removeAllView，添加到mRecyclerPool中
		从mRecyclerPool中获取、调用onBindViewHolder、调用addView