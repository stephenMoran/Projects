import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.TexturePaint;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.JList;

public class HarnessGUI
{
	//requires In class. Mock data is included in the project folder
	private JFrame frame;
	private JPanel addHarness;
	private JPanel loanHarness;
	private JPanel returnHarness;
	private JPanel removeHarness;
	private JPanel homeScreen;
	private JPanel checkHarness;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	//load in data
	In inputStream = new In("MOCK_DATA.txt");
	HarnessRecords newRecords = new HarnessRecords(inputStream);
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JList list;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					HarnessGUI window = new HarnessGUI();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public HarnessGUI()
	{
		initialize();
	}

	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 909, 511);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		// initializing panels
		homeScreen = new JPanel();
		homeScreen.setBackground(SystemColor.textHighlight);
		homeScreen.setForeground(Color.BLUE);
		frame.getContentPane().add(homeScreen, "name_163171662347456");
		homeScreen.setLayout(null);

		addHarness = new JPanel();
		addHarness.setBackground(SystemColor.textHighlight);
		frame.getContentPane().add(addHarness, "name_163174601805432");
		addHarness.setLayout(null);

		loanHarness = new JPanel();
		loanHarness.setBackground(SystemColor.textHighlight);
		frame.getContentPane().add(loanHarness, "name_163177486495209");
		loanHarness.setLayout(null);

		returnHarness = new JPanel();
		returnHarness.setBackground(SystemColor.textHighlight);
		frame.getContentPane().add(returnHarness, "name_163652405611456");
		returnHarness.setLayout(null);

		removeHarness = new JPanel();
		removeHarness.setBackground(SystemColor.textHighlight);
		frame.getContentPane().add(removeHarness, "name_163658548392691");
		removeHarness.setLayout(null);

		checkHarness = new JPanel();
		checkHarness.setBackground(SystemColor.textHighlight);
		frame.getContentPane().add(checkHarness, "name_164266901646222");
		checkHarness.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(182, 10, 700, 450);
		homeScreen.add(scrollPane);

		ArrayList<Harness> harnessList = newRecords.getHarnessList();
		list = new JList(harnessList.toArray());
		list.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane.setViewportView(list);

		// add harness button
		JButton btnAddHarness = new JButton("Add Harness");
		btnAddHarness.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addHarness.setVisible(true);
				homeScreen.setVisible(false);
			}
		});
		btnAddHarness.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAddHarness.setBounds(26, 28, 129, 25);
		homeScreen.add(btnAddHarness);

		// loan harness button
		JButton btnLoanHarness = new JButton("Loan Harness");
		btnLoanHarness.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				loanHarness.setVisible(true);
				homeScreen.setVisible(false);
			}
		});
		btnLoanHarness.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLoanHarness.setBounds(26, 97, 129, 25);
		homeScreen.add(btnLoanHarness);

		// remove harness button
		JButton btnRemove = new JButton("Remove Harness");
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				removeHarness.setVisible(true);
				homeScreen.setVisible(false);
			}
		});
		btnRemove.setBounds(26, 166, 129, 25);
		homeScreen.add(btnRemove);

		// check harness button
		JButton btnNewButton = new JButton("Check Harness");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				checkHarness.setVisible(true);
				homeScreen.setVisible(false);
			}
		});
		btnNewButton.setBounds(26, 231, 129, 25);
		homeScreen.add(btnNewButton);

		// return harness button
		JButton btnReturnHarness = new JButton("Return Harness");
		btnReturnHarness.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				returnHarness.setVisible(true);
				homeScreen.setVisible(false);
			}
		});
		btnReturnHarness.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnReturnHarness.setBounds(26, 302, 129, 25);
		homeScreen.add(btnReturnHarness);

		// Add harness components
		JButton btnNewButton_1 = new JButton("Add");
		btnNewButton_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String make = textField.getText();
				String modelNumberText = textField_1.getText();
				String lastCheckedBy = textField_2.getText();
				try
				{
					int modelNumber = Integer.parseInt(modelNumberText);
					Harness currentHarness = new Harness(make, modelNumber, lastCheckedBy);
					Harness addedHarness = newRecords.addHarness(currentHarness);
					if (addedHarness != null)
					{
						JOptionPane.showMessageDialog(null,
								currentHarness.toString() + "\nAdded to the harness records");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "This harness already exists");
					}
					updateList();
					addHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField.setText("");
					textField_1.setText("");
					textField_2.setText("");
				} catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Invlid entry, please try again");
					addHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField.setText("");
					textField_1.setText("");
					textField_2.setText("");
				}
			}
		});

		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_1.setBounds(392, 233, 118, 33);
		addHarness.add(btnNewButton_1);

		JLabel lblAddHarness = new JLabel("Add Harness");
		lblAddHarness.setBounds(219, 28, 165, 33);
		addHarness.add(lblAddHarness);

		JLabel lblMake = new JLabel("Make:");
		lblMake.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMake.setBounds(104, 89, 63, 33);
		addHarness.add(lblMake);

		JLabel lblNewLabel = new JLabel("Model number:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(104, 135, 126, 33);
		addHarness.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(252, 89, 226, 25);
		addHarness.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(252, 135, 226, 25);
		addHarness.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Last checked by:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(104, 180, 144, 25);
		addHarness.add(lblNewLabel_1);

		textField_2 = new JTextField();
		textField_2.setBounds(252, 179, 226, 26);
		addHarness.add(textField_2);
		textField_2.setColumns(10);

		// Loan harness components

		lblNewLabel_3 = new JLabel("Club Member:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_3.setBounds(51, 125, 115, 33);
		loanHarness.add(lblNewLabel_3);

		lblNewLabel_2 = new JLabel("Loan Harness");
		lblNewLabel_2.setBounds(216, 28, 160, 33);
		loanHarness.add(lblNewLabel_2);

		textField_3 = new JTextField();
		textField_3.setBounds(203, 131, 236, 27);
		loanHarness.add(textField_3);
		textField_3.setColumns(10);

		JButton btnNewButton_2 = new JButton("Loan");
		btnNewButton_2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					Harness currentHarness = newRecords.loanHarness(textField_3.getText());
					if (currentHarness == null)
					{
						JOptionPane.showMessageDialog(null,
								"Sorry there are no harnesses that fit this description available for loan");
					}
					else
					{
						JOptionPane.showMessageDialog(null,
								currentHarness.toString() + "\nHas been loaned to a club member");
					}
					updateList();
					loanHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField_3.setText("");

				} catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Invlid entry, please try again");
					loanHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField_3.setText("");

				}
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_2.setBounds(330, 216, 126, 27);
		loanHarness.add(btnNewButton_2);

		// return harness components
		JLabel lblReturnHarness = new JLabel("Return Harness");
		lblReturnHarness.setBounds(208, 28, 198, 33);
		returnHarness.add(lblReturnHarness);

		JLabel lblNewLabel_4 = new JLabel("Make:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(65, 102, 115, 33);
		returnHarness.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Model Number:");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_5.setBounds(65, 153, 130, 33);
		returnHarness.add(lblNewLabel_5);

		textField_6 = new JTextField();
		textField_6.setBounds(208, 101, 236, 26);
		returnHarness.add(textField_6);
		textField_6.setColumns(10);

		textField_7 = new JTextField();
		textField_7.setBounds(208, 164, 236, 26);
		returnHarness.add(textField_7);
		textField_7.setColumns(10);

		JButton btnNewButton_4 = new JButton("Return");
		btnNewButton_4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String make = textField_6.getText();
					int modelNumber = Integer.parseInt(textField_7.getText());
					Harness currentHarness = newRecords.returnHarness(make, modelNumber);
					if (currentHarness == null)
					{
						JOptionPane.showMessageDialog(null, "Sorry there are no harnesses that fit this decription");
					}
					else
					{
						JOptionPane.showMessageDialog(null, currentHarness.toString() + "\nHas been returned");
					}
					updateList();
					returnHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField_6.setText("");
					textField_7.setText("");
				} catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Invlid entry, please try again");
					returnHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField_6.setText("");
					textField_7.setText("");
				}
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_4.setBounds(367, 250, 145, 33);
		returnHarness.add(btnNewButton_4);

		// remove harness components
		JLabel lblRemoveHarness = new JLabel("Remove Harness");
		lblRemoveHarness.setBounds(195, 28, 210, 33);
		removeHarness.add(lblRemoveHarness);

		JLabel lblMake_1 = new JLabel("Make:");
		lblMake_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMake_1.setBounds(42, 118, 115, 33);
		removeHarness.add(lblMake_1);

		JLabel lblModelNumber = new JLabel("Model Number:");
		lblModelNumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblModelNumber.setBounds(42, 172, 126, 33);
		removeHarness.add(lblModelNumber);

		textField_4 = new JTextField();
		textField_4.setBounds(183, 118, 236, 26);
		removeHarness.add(textField_4);
		textField_4.setColumns(10);

		textField_5 = new JTextField();
		textField_5.setBounds(183, 179, 236, 26);
		removeHarness.add(textField_5);
		textField_5.setColumns(10);

		JButton btnNewButton_3 = new JButton("Remove Harness");
		btnNewButton_3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String make = textField_4.getText();
					int modelNumber = Integer.parseInt(textField_5.getText());
					Harness currentHarness = newRecords.removeHarness(make, modelNumber);
					if (currentHarness == null)
					{
						JOptionPane.showMessageDialog(null, "Sorry there are no harnesses that fit this decription");
					}
					else
					{
						JOptionPane.showMessageDialog(null,
								currentHarness.toString() + "\nHas been remove from records");
					}
					updateList();
					removeHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField_4.setText("");
					textField_5.setText("");
				} catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Invlid entry, please try again");
					removeHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField_4.setText("");
					textField_5.setText("");
				}
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_3.setBounds(350, 256, 153, 33);
		removeHarness.add(btnNewButton_3);

		// check harness components

		JLabel lblCheckHarness = new JLabel("Check Harness");
		lblCheckHarness.setBounds(218, 28, 195, 33);
		checkHarness.add(lblCheckHarness);

		JLabel lblNewLabel_6 = new JLabel("Instructor name:");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_6.setBounds(26, 80, 142, 33);
		checkHarness.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("Make:");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_7.setBounds(26, 131, 115, 33);
		checkHarness.add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel("Model Number");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_8.setBounds(26, 181, 115, 33);
		checkHarness.add(lblNewLabel_8);

		textField_8 = new JTextField();
		textField_8.setBounds(188, 86, 236, 25);
		checkHarness.add(textField_8);
		textField_8.setColumns(10);

		textField_9 = new JTextField();
		textField_9.setBounds(188, 139, 236, 25);
		checkHarness.add(textField_9);
		textField_9.setColumns(10);

		textField_10 = new JTextField();
		textField_10.setBounds(188, 195, 236, 25);
		checkHarness.add(textField_10);
		textField_10.setColumns(10);

		JButton btnNewButton_5 = new JButton("Check");
		btnNewButton_5.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String instructorName = textField_8.getText();
					String make = textField_9.getText();
					int modelNumber = Integer.parseInt(textField_10.getText());
					System.out.println(modelNumber);
					Harness checkedHarness = newRecords.checkHarness(instructorName, make, modelNumber);
					if (checkedHarness == null)
					{
						JOptionPane.showMessageDialog(null, "Sorry there are no harnesses that fit this decription");
					}
					else
					{
						JOptionPane.showMessageDialog(null, checkedHarness.toString() + "\nHas been checked");
					}
					updateList();
					checkHarness.setVisible(false);
					homeScreen.setVisible(true);
					textField_8.setText("");
					textField_9.setText("");
					textField_10.setText("");
				} catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Invlid entry, please try again");
					textField_8.setText("");
					textField_9.setText("");
					textField_10.setText("");
					checkHarness.setVisible(false);
					homeScreen.setVisible(true);
				}
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_5.setBounds(366, 248, 142, 33);
		checkHarness.add(btnNewButton_5);

	}

	public void updateList()
	{
		ArrayList<Harness> harnessList = newRecords.getHarnessList();
		list.setListData(harnessList.toArray());
	}
}
