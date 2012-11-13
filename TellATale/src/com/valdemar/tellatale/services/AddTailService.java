package com.valdemar.tellatale.services;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.valdemar.tellatale.model.Tail;

public class AddTailService {

	public interface OnAddTailListner {
		public void onSuccess();

		public void onFailure();
	}

	private OnAddTailListner onAddTailListner;

	public void setOnAddTailListner(OnAddTailListner onAddTailListner) {
		this.onAddTailListner = onAddTailListner;
	}

	public AddTailService(OnAddTailListner onAddTailListner) {
		setOnAddTailListner(onAddTailListner);
	}

	public void doAddTail(Tail tail, String taleID) {

		//VP: Teste refactor
		tail.setTale(taleID);
		tail.save(new StackMobCallback() {
			
			@Override
			public void success(String arg0) {
				onAddTailListner.onSuccess();
				
			}
			
			@Override
			public void failure(StackMobException arg0) {
				onAddTailListner.onFailure();
				
			}
		});
		
		/*
		StackMob stackMob = StackMobCommon.getStackMobInstance();

		List<Tail> items = Arrays.asList(tail);
		stackMob.postRelatedBulk("Tale", taleID, "tails", items, new StackMobCallback() {
			@Override
			public void success(String responseBody) {
				onAddTailListner.onSuccess();
			}

			@Override
			public void failure(StackMobException e) {
				onAddTailListner.onFailure();
			}
		});
		*/
		
	}

}
